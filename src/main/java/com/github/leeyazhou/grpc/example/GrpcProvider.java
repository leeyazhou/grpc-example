/**
 * 
 */
package com.github.leeyazhou.grpc.example;

import java.io.IOException;

import com.github.leeyazhou.grpc.core.GrpcServer;

/**
 * @author leeyazhou
 *
 */
public class GrpcProvider {

	public static void main(String[] args) throws IOException {
		int port = 8000;
		GrpcServer server = new GrpcServer(port);
		server.start();
	}
}
