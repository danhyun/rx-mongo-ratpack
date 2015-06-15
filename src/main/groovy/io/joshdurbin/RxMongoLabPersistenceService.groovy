package io.joshdurbin

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId

import static com.mongodb.client.model.Filters.*;

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import org.bson.Document
import rx.Observable

class RxMongoLabPersistenceService {

  MongoClient mongoClient
  MongoDatabase db

  RxMongoLabPersistenceService() {

    mongoClient = MongoClients.create("mongodb://demouser:demopass@ds045882.mongolab.com:45882/testdb")
    //mongoClient = MongoClients.create()
    db = mongoClient.getDatabase('testdb')

  }

  public Observable<Document> getPeople() {

    db.getCollection('people').find().toObservable()
  }

  public Observable<UpdateResult> updatePerson(String id, String firstName, String lastName, Integer age) {

    db.getCollection('people').updateOne(eq("_id", new ObjectId(id)),
      new Document('$set', new Document("firstName", firstName))
        .append('$set', new Document("lastName", lastName))
        .append('$set', new Document("age", age))).asObservable()
  }

  public Observable<Document> getPerson(String id) {

    db.getCollection('people').find(eq("_id", new ObjectId(id))).first()
  }

  public Observable<DeleteResult> removePerson(String id) {

    db.getCollection('people').deleteOne(eq("_id", new ObjectId(id))).first()
  }

  public Observable<Success> insertPerson(String firstName, String lastName, Integer age) {

    Document document = new Document()

    document.append("firstName", firstName)
    document.append("lastName", lastName)
    document.append("age", age)

    db.getCollection('people').insertOne(document)
  }
}
