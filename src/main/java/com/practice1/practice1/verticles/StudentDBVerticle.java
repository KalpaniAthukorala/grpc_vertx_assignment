package com.practice1.practice1.verticles;

import com.practice1.practice1.util.EventBusAddresses;
import com.practice1.practice1.util.SQLConstants;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Tuple;

public class StudentDBVerticle extends AbstractVerticle {

  private JDBCPool jdbcpool;

  @Override
  public void start() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("url", "jdbc:mysql://localhost:3306/student");
    jsonObject.put("driver_class", "com.mysql.cj.jdbc.Driver");
    jsonObject.put("max_pool_size", 30);
    jsonObject.put("user", "root");
    jdbcpool = JDBCPool.pool(vertx,jsonObject);

    vertx.eventBus().consumer(EventBusAddresses.ADDRESS_SAVE,message -> {
    JsonObject student = (JsonObject) message.body();
    saveStudent(student)
      .onSuccess(success->{
        message.reply(success);
      }).onFailure(throwable -> {
        message.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "Error while saving");
      });
    });
  }

  public Future<String> saveStudent(JsonObject student){
    Promise<String> promise = Promise.promise();
    String id = student.getString("id");
    String name = student.getString("name");
    jdbcpool.preparedQuery(SQLConstants.INSERT_STUDENT)
      .execute(Tuple.of(id,name))
      .onFailure(throwable -> {
        promise.fail("Failed");
      }).onSuccess(rows -> {
        if(rows.rowCount()>0){
          promise.complete("Data Saved Successfully");
        }else{
          promise.fail("Error occurred");
        }
      });
    return promise.future();
  }
}
