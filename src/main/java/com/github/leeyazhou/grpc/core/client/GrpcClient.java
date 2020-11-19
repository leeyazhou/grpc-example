package com.github.leeyazhou.grpc.core.client;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.grpc.MessageServiceGrpc;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.RpcResult;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class GrpcClient {
	private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);
	private final Serializer serializer = new JSONSerializer();
	private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;
	private final MessageServiceGrpc.MessageServiceStub messageServiceStub;
	private static final Map<String, GrpcClient> clientCache = new ConcurrentHashMap<>();

	public static GrpcClient get(String host, int port) {
		final String key = host + ":" + port;
		return clientCache.compute(key, (k1, v1) -> {
			return v1 != null ? v1 : new GrpcClient(host, port);
		});
	}

	public GrpcClient(String host, int port) {
		ManagedChannel managedChannel = NettyChannelBuilder.forAddress(host, port)
		// 使用非安全机制传输
//				.usePlaintext()
				.negotiationType(NegotiationType.TLS)

				.sslContext(buildSslContext()).build();
		this.blockingStub = MessageServiceGrpc.newBlockingStub(managedChannel);
		this.messageServiceStub = MessageServiceGrpc.newStub(managedChannel);
	}

	SslContext buildSslContext() {
		String trustCertCollectionFilePath =Thread.currentThread().getContextClassLoader().getResource("ca.crt").getFile();
		String clientCertChainFilePath = Thread.currentThread().getContextClassLoader().getResource("client.crt").getFile();
		String clientPrivateKeyFilePath = Thread.currentThread().getContextClassLoader().getResource("client.pem").getFile();
		SslContextBuilder builder = GrpcSslContexts.forClient();
		if (trustCertCollectionFilePath != null) {
			builder.trustManager(new File(trustCertCollectionFilePath));
		}
		if (clientCertChainFilePath != null && clientPrivateKeyFilePath != null) {
			builder.keyManager(new File(clientCertChainFilePath), new File(clientPrivateKeyFilePath));
		}
		try {
			return builder.build();
		} catch (SSLException e) {
			logger.error("", e);
		}
		return null;
	}

	public Response request(Invocation invocation) {
		RequestGrpcMessage greeting = buildMessage(invocation);
		ResponseGrpcMessage resp = blockingStub.request(greeting);
		byte[] responseByte = resp.getResponse().toByteArray();
		return serializer.deserialize(responseByte, Response.class);
	}

	RequestGrpcMessage buildMessage(Invocation invocation) {
		byte[] jsonBytes = serializer.serialize(invocation);
		ByteString byteString = ByteString.copyFrom(jsonBytes);

		RequestGrpcMessage greeting = RequestGrpcMessage.newBuilder().setInvocation(byteString).build();
		return greeting;
	}

	public Response streamingRequest(Invocation invocation) {
		RpcResult result = new RpcResult();
		ClientResponseObserver<RequestGrpcMessage, ResponseGrpcMessage> clientResponseObserver = new ClientResponseObserver<RequestGrpcMessage, ResponseGrpcMessage>() {
			ClientCallStreamObserver<RequestGrpcMessage> requestStream;

			@Override
			public void onNext(ResponseGrpcMessage response) {
				logger.info("响应结果：" + response);
				requestStream.request(1);
				result.setResult(response);
			}

			@Override
			public void onError(Throwable t) {
				logger.error("", t);
			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void beforeStart(ClientCallStreamObserver<RequestGrpcMessage> requestStream) {
				this.requestStream = requestStream;
				requestStream.disableAutoRequestWithInitial(1);
				requestStream.setOnReadyHandler(new Runnable() {
					@Override
					public void run() {
						while (requestStream.isReady()) {
							RequestGrpcMessage message = null;
							try {
								if ((message = messages.poll()) != null) {
									logger.info("request");
									requestStream.onNext(message);
								} else {
									requestStream.onCompleted();
								}
							} catch (Exception e) {
								requestStream.onError(e);
								logger.error("", e);
							}
						}
					}
				});
			}
		};
		RequestGrpcMessage message = buildMessage(invocation);
		messages.add(message);
		
		messageServiceStub.requestStreaming(clientResponseObserver);

		ResponseGrpcMessage resp = (ResponseGrpcMessage) result.getResult(10000);
		byte[] responseByte = resp.getResponse().toByteArray();
		return serializer.deserialize(responseByte, Response.class);
	}

	private ArrayBlockingQueue<RequestGrpcMessage> messages = new ArrayBlockingQueue<RequestGrpcMessage>(100000);
}