package com.github.leeyazhou.grpc;

import com.github.leeyazhou.grpc.MessageServiceGrpc.MessageServiceImplBase;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class GrpcServerHandler extends MessageServiceImplBase {

	@Override
	public void request(RequestGrpcMessage request, StreamObserver<ResponseGrpcMessage> responseObserver) {
		ByteString response = getResponse(request);
		ResponseGrpcMessage resp = ResponseGrpcMessage.newBuilder()

				.setResponse(response).build();
		responseObserver.onNext(resp);
		responseObserver.onCompleted();
	}

	ByteString getResponse(RequestGrpcMessage request) {
		System.out.println(request.getInvocation());
		return ByteString.copyFromUtf8("Hello " + request.getInvocation() + "!");
	}
}