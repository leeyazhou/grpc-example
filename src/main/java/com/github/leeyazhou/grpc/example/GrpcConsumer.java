/**
 * 
 */
package com.github.leeyazhou.grpc.example;

import com.github.leeyazhou.grpc.core.GrpcClient;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;

/**
 * @author leeyazhou
 *
 */
public class GrpcConsumer {

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 8000;
		GrpcClient client = new GrpcClient(host, port);

		Invocation invocation = new Invocation();
		invocation.setServiceName("echoService");
		invocation.setMethodName("echo");
		Response response = client.request(invocation);
		System.out.println(response);
	}
}
