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
		String host = "localhost";
		int port = 8000;
		GrpcClient client = GrpcClient.get(host, port);

		int i = 0;
		while (i++ < 10) {
			Invocation invocation = new Invocation();
			invocation.setServiceName("EchoService");
			invocation.setMethodName("echo");
			invocation.setArgs(new String[] { "测试GRPC" });
			Response response = client.streamingRequest(invocation);
			System.out.println(response.getResponse());
			try {
//				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
