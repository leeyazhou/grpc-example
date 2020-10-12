package com.github.leeyazhou.grpc.example;

import com.alibaba.fastjson.JSON;
import com.github.leeyazhou.grpc.core.GrpcClient;
import com.github.leeyazhou.grpc.core.GrpcServer;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.example.service.EchoService;
import com.github.leeyazhou.grpc.example.service.EchoServiceImpl;

public class HelloWorldApp {

	public static void main(String[] args) throws Exception {
		int port = 8000;
		GrpcServer server = new GrpcServer(port);
		server.addService(EchoService.class.getSimpleName(), new EchoServiceImpl());
		server.start();
		GrpcClient client = new GrpcClient("127.0.0.1", port);

		Invocation invocation = new Invocation();
		invocation.setServiceName(EchoService.class.getSimpleName());
		invocation.setMethodName("echo");
		invocation.setArgs(new String[] { "测试GRPC" });
		Response response = client.request(invocation);
		System.out.println(JSON.toJSONString(response));
		server.shutdown();
	}

}