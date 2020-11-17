package com.github.leeyazhou.grpc.core.client;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.grpc.MessageServiceGrpc;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class GrpcClient {
	private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);
	private final Serializer serializer = new JSONSerializer();
	private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;
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
	}

	SslContext buildSslContext() {
		String trustCertCollectionFilePath = "/opt/code/git/grpc-example/src/main/resources/ca.crt";
		String clientCertChainFilePath = "/opt/code/git/grpc-example/src/main/resources/client.crt";
		String clientPrivateKeyFilePath = "/opt/code/git/grpc-example/src/main/resources/client.pem";
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
		byte[] jsonBytes = serializer.serialize(invocation);
		ByteString byteString = ByteString.copyFrom(jsonBytes);

		RequestGrpcMessage greeting = RequestGrpcMessage.newBuilder().setInvocation(byteString).build();
		ResponseGrpcMessage resp = blockingStub.request(greeting);
		byte[] responseByte = resp.getResponse().toByteArray();
		return serializer.deserialize(responseByte, Response.class);
	}
}