package com.practice1.practice1.verticles;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import static com.practice1.practice1.util.EventBusAddresses.ADDRESS_SAVE;
import static com.practice1.practice1.util.EventBusAddresses.ADDRESS_THIRD_PARTY;

public class StudentHttpVerticle extends AbstractVerticle {



  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route(HttpMethod.POST,"/save/student").handler(this::saveStudent);
    vertx.createHttpServer().requestHandler(router).listen(8080);
  }

  private void saveStudent(RoutingContext routingContext) {
    JsonObject student = routingContext.getBodyAsJson();
    vertx.eventBus().request(ADDRESS_SAVE,student,reply->{
      if(reply.succeeded()){
        System.out.println(reply.result().body());
        routingContext.response().end((String) reply.result().body());
        callThirdParty(student);
      }else{
        routingContext.response().end(reply.cause().getMessage());
      }
    });
  }

  public void callThirdParty(JsonObject student){
    vertx.eventBus().request(ADDRESS_THIRD_PARTY,student);
  }
}
