# grpc-example

基于gRPC实现的简单rpc框架

## 配置

### 属性配置

pom.xml中配置依赖的gRPC版本号

```
	<properties>
		<grpc.version>1.32.1</grpc.version>
		<!-- Message源文件输出目录 -->
		<javaOutputDirectory>${project.basedir}/src/main/java-proto</javaOutputDirectory>
		<!-- gRPC源文件输出目录 -->
		<protocPluginOutputDirectory>${project.basedir}/src/main/java-grpc</protocPluginOutputDirectory>
	</properties>
```

### Maven依赖

```
	<dependencies>
		<dependency>
			<groupId>io.grpc</groupId>
			<artifactId>grpc-netty</artifactId>
			<version>${grpc.version}</version>
		</dependency>
		<dependency>
			<groupId>io.grpc</groupId>
			<artifactId>grpc-protobuf</artifactId>
			<version>${grpc.version}</version>
		</dependency>
		<dependency>
			<groupId>io.grpc</groupId>
			<artifactId>grpc-stub</artifactId>
			<version>${grpc.version}</version>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>fastjson</artifactId>
			<version>1.2.74</version>
		</dependency>
		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>1.2.3</version>
		</dependency>

	</dependencies>
```

### Maven插件

```
	<build>
		<extensions>
			<extension>
				<groupId>kr.motd.maven</groupId>
				<artifactId>os-maven-plugin</artifactId>
				<version>1.6.2</version>
			</extension>
		</extensions>
		<plugins>
			<plugin>
				<groupId>org.xolstice.maven.plugins</groupId>
				<artifactId>protobuf-maven-plugin</artifactId>
				<version>0.6.1</version>
				<configuration>
					<protocArtifact>
						com.google.protobuf:protoc:3.13.0:exe:${os.detected.classifier}
					</protocArtifact>
					<pluginId>grpc-java</pluginId>
					<pluginArtifact>
						io.grpc:protoc-gen-grpc-java:1.32.1:exe:${os.detected.classifier}
					</pluginArtifact>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>compile</goal>
							<goal>compile-custom</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```

## 框架开发

### Protobuf文件

创建文件 src/main/proto/crpc_protocol.proto

```
syntax = "proto3";

option java_package = "com.github.leeyazhou.grpc";
option java_multiple_files = true;
option java_outer_classname = "CrpcProtocol";

message RequestGrpcMessage {
    bytes invocation = 1;
}

message ResponseGrpcMessage {
    bytes response = 1;
}

service MessageService {
    rpc request (RequestGrpcMessage) returns (ResponseGrpcMessage);
}
```

执行如下命令会生成Protobuf文件对应的Java类

```
mvn protobuf:compile 
mvn protobuf:compile-custom
```

### 公用基础模型类

Invocation.java

```
package com.github.leeyazhou.grpc.core;

public class Invocation {

	private String serviceName;
	private String methodName;
	private Object[] args;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	
}

```

Response.java

```
package com.github.leeyazhou.grpc.core;

public class Response {

	private boolean error;
	private Object response;
	private Throwable exception;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

}

```

### Server代码

GrpcServer.java 

```
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
```

GrpcServerHandler.java负责处理接收到的请求，并转发给ServiceHandler.java处理，处理完成后响应给请求端。

```
package com.github.leeyazhou.grpc.core.server;

import com.github.leeyazhou.grpc.MessageServiceGrpc.MessageServiceImplBase;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class GrpcServerHandler extends MessageServiceImplBase {
	private ServiceHandler serviceHandler;
	private Serializer serializer = new JSONSerializer();

	public GrpcServerHandler(ServiceHandler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	@Override
	public void request(RequestGrpcMessage request, StreamObserver<ResponseGrpcMessage> responseObserver) {
		try {
			final Invocation invocation = serializer.deserialize(request.getInvocation().toByteArray(),
					Invocation.class);
			final Response response = handleRequest(invocation);

			byte[] jsonByte = serializer.serialize(response);
			ResponseGrpcMessage resp = ResponseGrpcMessage.newBuilder().setResponse(ByteString.copyFrom(jsonByte))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	private Response handleRequest(Invocation invocation) {
		Response response = new Response();
		response.setError(false);
		try {
			Object ret = serviceHandler.handle(invocation);
			response.setResponse(ret);
		} catch (Exception e) {
			response.setError(true);
			response.setException(e);
		}
		return response;
	}
}
```

ServiceHandler.java

```
package com.github.leeyazhou.grpc.core.server;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.leeyazhou.grpc.core.Invocation;

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
```

### Client代码

GrpcClient.java

```
package com.github.leeyazhou.grpc.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.leeyazhou.grpc.MessageServiceGrpc;
import com.github.leeyazhou.grpc.RequestGrpcMessage;
import com.github.leeyazhou.grpc.ResponseGrpcMessage;
import com.github.leeyazhou.grpc.core.Invocation;
import com.github.leeyazhou.grpc.core.Response;
import com.github.leeyazhou.grpc.core.serializer.JSONSerializer;
import com.github.leeyazhou.grpc.core.serializer.Serializer;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
	private final Serializer serializer = new JSONSerializer();
	private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;
	private static final Map<String, GrpcClient> clientCache = new ConcurrentHashMap<>();

	public static GrpcClient get(String host, int port) {
		final String key = host + ":" + port;
		return clientCache.compute(key, (k1, v1) -> {
			return v1 != null ? v1 : new GrpcClient(host, port);
		});
	}

	public GrpcClient(String host, int port) {
		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
				// 使用非安全机制传输
				.usePlaintext().build();
		this.blockingStub = MessageServiceGrpc.newBlockingStub(managedChannel);
	}

	public Response request(Invocation invocation) {
		byte[] jsonBytes = serializer.serialize(invocation);
		ByteString byteString = ByteString.copyFrom(jsonBytes);

		RequestGrpcMessage greeting = RequestGrpcMessage.newBuilder().setInvocation(byteString).build();
		ResponseGrpcMessage resp = blockingStub.request(greeting);
		byte[] responseByte = resp.getResponse().toByteArray();
		return serializer.deserialize(responseByte, Response.class);
	}
}
```

## 示例

### 服务类开发

EchoService.java定义服务接口

```
public interface EchoService {
	String echo(String echo);
}
```

EchoServiceImpl.java实现服务功能：

```
public class EchoServiceImpl implements EchoService {
	@Override
	public String echo(String echo) {
		System.err.println("回声: " + echo);
		return echo;
	}
}
```

### 启动服务端

GrpcProvider.java 启动Server服务，并监听端口8000

```
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
```

### 客户端调用服务

```
public class GrpcConsumer {

	public static void main(String[] args) {
		new GrpcConsumer().start();
	}

	public void start() {
		String host = "127.0.0.1";
		int port = 8000;
		GrpcClient client = GrpcClient.get(host, port);

		Invocation invocation = new Invocation();
		invocation.setServiceName("EchoService");
		invocation.setMethodName("echo");
		invocation.setArgs(new String[] { "测试GRPC" });
		Response response = client.request(invocation);
		System.out.println(response.getResponse());
	}
}
```

## 其他

源码地址[github.com/leeyazhou/grpc-example](github.com/leeyazhou/grpc-example)