package com.github.leeyazhou.grpc.core;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcResult {
	private static final Logger logger = LoggerFactory.getLogger(RpcResult.class);
	private Object result;
	private CountDownLatch latch = new CountDownLatch(1);

	public Object getResult() {
		return getResult(3000);
	}

	public Object getResult(int timeout) {
		try {
			latch.await(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
		latch.countDown();
	}

}
