/**
 * 
 */
package com.github.leeyazhou.grpc.example;

import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.client.GrpcClient;

/**
 * @author leeyazhou
 *
 */
public class GrpcConsumer {

	public static void main(String[] args) {
		new GrpcConsumer().start();
	}

	public void start() {
		String host = "127.0.0.1";
		int port = 8000;
		GrpcClient client = GrpcClient.get(host, port);

		Invocation invocation = new Invocation();
		invocation.setServiceName("EchoService");
		invocation.setMethodName("echo");
		invocation.setArgs(new String[] { "测试GRPC" });
		Response response = client.request(invocation);
		System.out.println(response.getResponse());
	}
}
