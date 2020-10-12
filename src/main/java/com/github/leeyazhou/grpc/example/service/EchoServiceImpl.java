package com.github.leeyazhou.grpc.example.service;

public class EchoServiceImpl implements EchoService {

	@Override
	public String echo(String echo) {
		System.err.println("回声: " + echo);
		return echo;
	}

}
