package com.github.leeyazhou.grpc.core;

import com.alibaba.fastjson.JSON;
import com.github.leeyazhou.grpc.MessageServiceGrpc;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
	private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;

	public GrpcClient(String host, int port) {
		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
				// 使用非安全机制传输
				.usePlaintext().build();
		this.blockingStub = MessageServiceGrpc.newBlockingStub(managedChannel);
	}

	public Response request(Invocation invocation) {
		byte[] jsonBytes = JSON.toJSONBytes(invocation);
		ByteString byteString = ByteString.copyFrom(jsonBytes);

		RequestGrpcMessage greeting = RequestGrpcMessage.newBuilder().setInvocation(byteString).build();
		ResponseGrpcMessage resp = blockingStub.request(greeting);
		byte[] responseByte = resp.getResponse().toByteArray();
		return JSON.parseObject(responseByte, Response.class);
	}
}