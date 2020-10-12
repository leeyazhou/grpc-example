package com.github.leeyazhou.grpc.core.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
	private Server server;
	private ServiceHandler serviceHandler;

	public GrpcServer(int port) {
		this.serviceHandler = new ServiceHandler();
		this.server = ServerBuilder.forPort(port)
				// 将具体实现的服务添加到gRPC服务中
				.addService(new GrpcServerHandler(serviceHandler))

				.build();
	}

	public GrpcServer addService(String name, Object service) {
		serviceHandler.addService(name, service);
		return this;
	}

	public void start() throws IOException {
		server.start();
	}

	public void shutdown() {
		server.shutdown();
	}
}