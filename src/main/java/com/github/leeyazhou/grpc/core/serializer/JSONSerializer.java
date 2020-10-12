package com.github.leeyazhou.grpc.core.serializer;

import com.alibaba.fastjson.JSON;

public class JSONSerializer implements Serializer {

	@Override
	public byte[] serialize(Object data) {
		return JSON.toJSONBytes(data);
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> type) {
		return JSON.parseObject(data, type);
	}

}
