/**
 * 
 */
package com.github.leeyazhou.grpc.example;

import com.github.leeyazhou.grpc.core.server.GrpcServer;
import com.github.leeyazhou.grpc.example.service.EchoService;
import com.github.leeyazhou.grpc.example.service.EchoServiceImpl;

/**
 * @author leeyazhou
 *
 */
public class GrpcProvider {

	public static void main(String[] args) throws Exception {
		new GrpcProvider().start();
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void start() throws Exception {
		int port = 8000;
		GrpcServer server = new GrpcServer(port);
		server.addService(EchoService.class.getSimpleName(), new EchoServiceImpl());
		server.start();
	}
}
