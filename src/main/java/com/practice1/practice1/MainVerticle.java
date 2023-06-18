package com.practice1.practice1;

import com.practice1.practice1.verticles.StudentDBVerticle;
import com.practice1.practice1.verticles.StudentHttpVerticle;
import com.practice1.practice1.verticles.ThirdPartyVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class MainVerticle{
  public static void main(String[] args) {
    Vertx vertx1 = Vertx.vertx();
    vertx1.deployVerticle(new StudentDBVerticle());
    vertx1.deployVerticle(new StudentHttpVerticle());
    vertx1.deployVerticle(new ThirdPartyVerticle());
  }
}
