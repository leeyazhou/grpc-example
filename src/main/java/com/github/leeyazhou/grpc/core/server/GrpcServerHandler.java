package com.github.leeyazhou.grpc.core.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.leeyazhou.grpc.MessageServiceGrpc.MessageServiceImplBase;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.google.protobuf.ByteString;

import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

public class GrpcServerHandler extends MessageServiceImplBase {
	private static final Logger logger = LoggerFactory.getLogger(GrpcServerHandler.class);
	private ServiceHandler serviceHandler;
	private Serializer serializer = new JSONSerializer();

	public GrpcServerHandler(ServiceHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	@Override
	public void request(RequestGrpcMessage request, StreamObserver<ResponseGrpcMessage> responseObserver) {
		try {
			ResponseGrpcMessage resp = handleRequest(request);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	@Override
	public StreamObserver<RequestGrpcMessage> requestStreaming(StreamObserver<ResponseGrpcMessage> responseObserver) {
		final ServerCallStreamObserver<ResponseGrpcMessage> serverCallStreamObserver = (ServerCallStreamObserver<ResponseGrpcMessage>) responseObserver;
		serverCallStreamObserver.disableAutoRequest();

		class OnReadyHandler implements Runnable {
			// Guard against spurious onReady() calls caused by a race between onNext() and
			// onReady(). If the transport
			// toggles isReady() from false to true while onNext() is executing, but before
			// onNext() checks isReady(),
			// request(1) would be called twice - once by onNext() and once by the onReady()
			// scheduled during onNext()'s
			// execution.
			private boolean wasReady = false;

			@Override
			public void run() {
				try {
					if (serverCallStreamObserver.isReady() && !wasReady) {
						wasReady = true;
						logger.info("READY");
						// Signal the request sender to send one message. This happens when isReady()
						// turns true, signaling that
						// the receive buffer has enough free space to receive more messages. Calling
						// request() serves to prime
						// the message pump.
						serverCallStreamObserver.request(1);
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}

		final OnReadyHandler onReadyHandler = new OnReadyHandler();
		serverCallStreamObserver.setOnReadyHandler(onReadyHandler);

		return new StreamObserver<RequestGrpcMessage>() {

			@Override
			public void onNext(RequestGrpcMessage request) {
				try {
					ResponseGrpcMessage resp = handleRequest(request);
					responseObserver.onNext(resp);

					if (serverCallStreamObserver.isReady()) {
						serverCallStreamObserver.request(1);
					} else {
						onReadyHandler.wasReady = false;
					}
				} catch (Exception e) {
					responseObserver.onError(e);
					logger.error("", e);
				}
			}

			@Override
			public void onError(Throwable t) {
				responseObserver.onError(t);
				responseObserver.onCompleted();
			}

			@Override
			public void onCompleted() {
				logger.info("COMPLETED");
				responseObserver.onCompleted();
			}
		};
	}

	ResponseGrpcMessage handleRequest(RequestGrpcMessage request) {
		final Invocation invocation = serializer.deserialize(request.getInvocation().toByteArray(), Invocation.class);
		final Response response = handleInvocation(invocation);

		byte[] jsonByte = serializer.serialize(response);
		ResponseGrpcMessage resp = ResponseGrpcMessage.newBuilder().setResponse(ByteString.copyFrom(jsonByte)).build();
		return resp;
	}

	private Response handleInvocation(Invocation invocation) {
		Response response = new Response();
		response.setError(false);
		try {
			Object ret = serviceHandler.handle(invocation);
			response.setResponse(ret);
		} catch (Exception e) {
			response.setError(true);
			response.setException(e);
		}
		return response;
	}
}