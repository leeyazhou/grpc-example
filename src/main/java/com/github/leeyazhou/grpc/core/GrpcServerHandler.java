package com.github.leeyazhou.grpc.core;

import com.alibaba.fastjson.JSON;
import com.github.leeyazhou.grpc.MessageServiceGrpc.MessageServiceImplBase;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class GrpcServerHandler extends MessageServiceImplBase {
	private ServiceHandler serviceHandler;

	public GrpcServerHandler(ServiceHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	@Override
	public void request(RequestGrpcMessage request, StreamObserver<ResponseGrpcMessage> responseObserver) {
		ByteString response = getResponse(request);
		ResponseGrpcMessage resp = ResponseGrpcMessage.newBuilder()

				.setResponse(response).build();
		responseObserver.onNext(resp);
		responseObserver.onCompleted();
	}

	ByteString getResponse(RequestGrpcMessage request) {
		Invocation invocation = JSON.parseObject(request.getInvocation().toByteArray(), Invocation.class);
		Response response = new Response();
		response.setError(false);
		try {
			Object ret = serviceHandler.handle(invocation);
			response.setResponse(ret);
		} catch (Exception e) {
			response.setError(true);
			response.setException(e);
		}

		byte[] jsonByte = JSON.toJSONBytes(response);
		return ByteString.copyFrom(jsonByte);
	}
}