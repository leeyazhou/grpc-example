package com.github.leeyazhou.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.32.1)",
    comments = "Source: crpc_protocol.proto")
public final class MessageServiceGrpc {

  private MessageServiceGrpc() {}

  public static final String SERVICE_NAME = "MessageService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.github.leeyazhou.grpc.RequestGrpcMessage,
      com.github.leeyazhou.grpc.ResponseGrpcMessage> getRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "request",
      requestType = com.github.leeyazhou.grpc.RequestGrpcMessage.class,
      responseType = com.github.leeyazhou.grpc.ResponseGrpcMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.github.leeyazhou.grpc.RequestGrpcMessage,
      com.github.leeyazhou.grpc.ResponseGrpcMessage> getRequestMethod() {
    io.grpc.MethodDescriptor<com.github.leeyazhou.grpc.RequestGrpcMessage, com.github.leeyazhou.grpc.ResponseGrpcMessage> getRequestMethod;
    if ((getRequestMethod = MessageServiceGrpc.getRequestMethod) == null) {
      synchronized (MessageServiceGrpc.class) {
        if ((getRequestMethod = MessageServiceGrpc.getRequestMethod) == null) {
          MessageServiceGrpc.getRequestMethod = getRequestMethod =
              io.grpc.MethodDescriptor.<com.github.leeyazhou.grpc.RequestGrpcMessage, com.github.leeyazhou.grpc.ResponseGrpcMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "request"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.leeyazhou.grpc.RequestGrpcMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.leeyazhou.grpc.ResponseGrpcMessage.getDefaultInstance()))
              .setSchemaDescriptor(new MessageServiceMethodDescriptorSupplier("request"))
              .build();
        }
      }
    }
    return getRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.github.leeyazhou.grpc.RequestGrpcMessage,
      com.github.leeyazhou.grpc.ResponseGrpcMessage> getRequestStreamingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "requestStreaming",
      requestType = com.github.leeyazhou.grpc.RequestGrpcMessage.class,
      responseType = com.github.leeyazhou.grpc.ResponseGrpcMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.github.leeyazhou.grpc.RequestGrpcMessage,
      com.github.leeyazhou.grpc.ResponseGrpcMessage> getRequestStreamingMethod() {
    io.grpc.MethodDescriptor<com.github.leeyazhou.grpc.RequestGrpcMessage, com.github.leeyazhou.grpc.ResponseGrpcMessage> getRequestStreamingMethod;
    if ((getRequestStreamingMethod = MessageServiceGrpc.getRequestStreamingMethod) == null) {
      synchronized (MessageServiceGrpc.class) {
        if ((getRequestStreamingMethod = MessageServiceGrpc.getRequestStreamingMethod) == null) {
          MessageServiceGrpc.getRequestStreamingMethod = getRequestStreamingMethod =
              io.grpc.MethodDescriptor.<com.github.leeyazhou.grpc.RequestGrpcMessage, com.github.leeyazhou.grpc.ResponseGrpcMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "requestStreaming"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.leeyazhou.grpc.RequestGrpcMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.github.leeyazhou.grpc.ResponseGrpcMessage.getDefaultInstance()))
              .setSchemaDescriptor(new MessageServiceMethodDescriptorSupplier("requestStreaming"))
              .build();
        }
      }
    }
    return getRequestStreamingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MessageServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MessageServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MessageServiceStub>() {
        @java.lang.Override
        public MessageServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MessageServiceStub(channel, callOptions);
        }
      };
    return MessageServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MessageServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MessageServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MessageServiceBlockingStub>() {
        @java.lang.Override
        public MessageServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MessageServiceBlockingStub(channel, callOptions);
        }
      };
    return MessageServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MessageServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MessageServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MessageServiceFutureStub>() {
        @java.lang.Override
        public MessageServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MessageServiceFutureStub(channel, callOptions);
        }
      };
    return MessageServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class MessageServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void request(com.github.leeyazhou.grpc.RequestGrpcMessage request,
        io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.ResponseGrpcMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.RequestGrpcMessage> requestStreaming(
        io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.ResponseGrpcMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getRequestStreamingMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.github.leeyazhou.grpc.RequestGrpcMessage,
                com.github.leeyazhou.grpc.ResponseGrpcMessage>(
                  this, METHODID_REQUEST)))
          .addMethod(
            getRequestStreamingMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.github.leeyazhou.grpc.RequestGrpcMessage,
                com.github.leeyazhou.grpc.ResponseGrpcMessage>(
                  this, METHODID_REQUEST_STREAMING)))
          .build();
    }
  }

  /**
   */
  public static final class MessageServiceStub extends io.grpc.stub.AbstractAsyncStub<MessageServiceStub> {
    private MessageServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MessageServiceStub(channel, callOptions);
    }

    /**
     */
    public void request(com.github.leeyazhou.grpc.RequestGrpcMessage request,
        io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.ResponseGrpcMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.RequestGrpcMessage> requestStreaming(
        io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.ResponseGrpcMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getRequestStreamingMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class MessageServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MessageServiceBlockingStub> {
    private MessageServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MessageServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.github.leeyazhou.grpc.ResponseGrpcMessage request(com.github.leeyazhou.grpc.RequestGrpcMessage request) {
      return blockingUnaryCall(
          getChannel(), getRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MessageServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MessageServiceFutureStub> {
    private MessageServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessageServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MessageServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.github.leeyazhou.grpc.ResponseGrpcMessage> request(
        com.github.leeyazhou.grpc.RequestGrpcMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST = 0;
  private static final int METHODID_REQUEST_STREAMING = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MessageServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MessageServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST:
          serviceImpl.request((com.github.leeyazhou.grpc.RequestGrpcMessage) request,
              (io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.ResponseGrpcMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_STREAMING:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.requestStreaming(
              (io.grpc.stub.StreamObserver<com.github.leeyazhou.grpc.ResponseGrpcMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MessageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MessageServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.github.leeyazhou.grpc.CrpcProtocol.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MessageService");
    }
  }

  private static final class MessageServiceFileDescriptorSupplier
      extends MessageServiceBaseDescriptorSupplier {
    MessageServiceFileDescriptorSupplier() {}
  }

  private static final class MessageServiceMethodDescriptorSupplier
      extends MessageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MessageServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MessageServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MessageServiceFileDescriptorSupplier())
              .addMethod(getRequestMethod())
              .addMethod(getRequestStreamingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
