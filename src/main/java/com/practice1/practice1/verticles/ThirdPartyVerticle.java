package com.practice1.practice1.verticles;
import com.practice1.practice1.util.EventBusAddresses;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;

public class ThirdPartyVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.eventBus().consumer(EventBusAddresses.ADDRESS_THIRD_PARTY, message -> {
      JsonObject student = (JsonObject) message.body();
      String name = student.getString("name");
      GrpcClient client = GrpcClient.client(vertx);
      client.request(SocketAddress.inetSocketAddress(8081, "localhost"), GreeterGrpc.getSayHelloMethod())
              .compose(request -> {
                request.end(HelloRequest.newBuilder().setName(name).build());
                return request.response().compose(GrpcReadStream::last);
              })
              .onSuccess(reply -> System.out.println("Successfully send student details to third party " + reply.getMessage()))
              .onFailure(Throwable::printStackTrace);
    });

  }
}

