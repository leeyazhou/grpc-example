package com.github.leeyazhou.grpc.core.serializer;

public interface Serializer {

	byte[] serialize(Object data);

	<T> T deserialize(byte[] data, Class<T> type);
}
