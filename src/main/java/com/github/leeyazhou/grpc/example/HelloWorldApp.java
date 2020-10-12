package com.github.leeyazhou.grpc.example;

public class HelloWorldApp {

	public static void main(String[] args) throws Exception {
		new GrpcProvider().start();
		new GrpcConsumer().start();
	}

}