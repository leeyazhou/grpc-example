package com.github.leeyazhou.grpc.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.leeyazhou.grpc.MessageServiceGrpc;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
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
		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
				// 使用非安全机制传输
				.usePlaintext().build();
		this.blockingStub = MessageServiceGrpc.newBlockingStub(managedChannel);
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