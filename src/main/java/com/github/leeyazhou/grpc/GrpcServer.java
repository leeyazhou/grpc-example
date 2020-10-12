package com.github.leeyazhou.grpc;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {

	private Server server;

	public GrpcServer(int port) {
		this.server = ServerBuilder.forPort(port)
				// 将具体实现的服务添加到gRPC服务中
				.addService(new GrpcServerHandler())

				.build();
	}

	public void start() throws IOException {
		server.start();
	}

	public void shutdown() {
		server.shutdown();
	}
}