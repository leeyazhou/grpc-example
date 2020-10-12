package com.github.leeyazhou.grpc.core.server;

import com.github.leeyazhou.grpc.MessageServiceGrpc.MessageServiceImplBase;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class GrpcServerHandler extends MessageServiceImplBase {
	private ServiceHandler serviceHandler;
	private Serializer serializer = new JSONSerializer();

	public GrpcServerHandler(ServiceHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	@Override
	public void request(RequestGrpcMessage request, StreamObserver<ResponseGrpcMessage> responseObserver) {
		try {
			final Invocation invocation = serializer.deserialize(request.getInvocation().toByteArray(),
					Invocation.class);
			final Response response = handleRequest(invocation);

			byte[] jsonByte = serializer.serialize(response);
			ResponseGrpcMessage resp = ResponseGrpcMessage.newBuilder().setResponse(ByteString.copyFrom(jsonByte))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	private Response handleRequest(Invocation invocation) {
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