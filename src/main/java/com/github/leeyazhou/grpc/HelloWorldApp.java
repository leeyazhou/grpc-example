package com.github.leeyazhou.grpc;

public class HelloWorldApp {

	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 8000;
		GrpcServer server = new GrpcServer(port);
		server.start();
		GrpcClient client = new GrpcClient(host, port);
		
		Invocation invocation = new Invocation();
		invocation.setServiceName("echoService");
		invocation.setMethodName("echo");
		Response response = client.request(invocation);
		System.out.println(response);
		server.shutdown();
	}

}