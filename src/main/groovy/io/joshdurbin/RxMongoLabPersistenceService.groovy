package io.joshdurbin

import com.google.inject.Inject
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import groovy.transform.CompileStatic
import org.bson.Document
import org.bson.types.ObjectId
import ratpack.exec.ExecControl
import ratpack.form.Form
import ratpack.rx.RxRatpack
import rx.Observable
import rx.functions.Func1

import static com.mongodb.client.model.Filters.eq

@CompileStatic
class RxMongoLabPersistenceService {

  MongoClient mongoClient
  MongoDatabase db

  ExecControl execControl
  Observable.Transformer<? super Document, ? extends Document> transformer

  @Inject
  RxMongoLabPersistenceService(ExecControl execControl) {
    this.execControl = execControl
    mongoClient = MongoClients.create("mongodb://localhost:27017/")
    //mongoClient = MongoClients.create()
    db = mongoClient.getDatabase('testdb')
    transformer = RxRatpack.&bindExec as Observable.Transformer<? super Document, ? extends Document>
  }

  public Observable<Document> getPeople() {

    db
      .getCollection('people')
      .find()
      .toObservable()
      .compose(transformer)
  }

  public Observable<UpdateResult> updatePerson(String id, String firstName, String lastName, Integer age) {
    db.getCollection('people').updateOne(eq("_id", new ObjectId(id)),
      new Document('$set', new Document("firstName", firstName))
        .append('$set', new Document("lastName", lastName))
        .append('$set', new Document("age", age))).asObservable()
  }

  public Observable<Document> getPerson(String id) {
      db
        .getCollection('people')
        .find(eq("_id", new ObjectId(id)))
        .first()
        .compose(transformer)
  }

  public Observable<DeleteResult> removePerson(String id) {
    db.getCollection('people').deleteOne(eq("_id", new ObjectId(id))).first()
  }

  public Observable<Document> insertPerson(Form form) {
    Document document =
            new Document()
              .append("firstName", form.firstName)
              .append("lastName", form.lastName)
              .append("age", form.age)

    db
      .getCollection('people')
      .insertOne(document)
      .map({Success success ->
        document
      } as Func1)
      .compose(transformer)
  }

}
