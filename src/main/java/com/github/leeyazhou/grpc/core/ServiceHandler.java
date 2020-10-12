package com.github.leeyazhou.grpc.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceHandler {
	private Map<String, Object> services = new ConcurrentHashMap<>();
	private Map<String, Method> serviceMethods = new ConcurrentHashMap<>();

	public Object handle(Invocation invocation) {
		Object service = services.get(invocation.getServiceName());
		Method serviceMethod = serviceMethods.get(invocation.getServiceName() + "$" + invocation.getMethodName());

		try {
			return serviceMethod.invoke(service, invocation.getArgs());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void addService(String name, Object service) {
		this.services.put(name, service);
		Method[] methods = service.getClass().getDeclaredMethods();
		if (methods != null) {
			for (Method method : methods) {
				String key = name + "$" + method.getName();
				serviceMethods.put(key, method);
			}
		}

	}
}
