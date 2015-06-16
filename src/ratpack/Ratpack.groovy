import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult

import org.bson.Document
import ratpack.form.Form
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.jackson.Jackson
import ratpack.jackson.JacksonModule
import ratpack.rx.RxRatpack
import ratpack.server.Service
import ratpack.server.StartEvent
import io.joshdurbin.PersistenceModule
import io.joshdurbin.RxMongoLabPersistenceService

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {

    module MarkupTemplateModule
    module JacksonModule

    module PersistenceModule

    bindInstance Service, new Service() {
      @Override
      void onStart(StartEvent event) throws Exception {
        RxRatpack.initialize()
      }
    }
  }

  handlers { RxMongoLabPersistenceService persistenceService ->

    handler("people") {

      byMethod {

        get {
          persistenceService.getPeople().toList().subscribe() { List<Document> people ->
            render Jackson.json(people)
          }
        }

        post {
          def form = parse(Form)

          persistenceService
              .insertPerson(form)
              .subscribe { Document document ->
                redirect "/person/${document.getObjectId('_id')}"
              }
        }
      }
    }

    handler("person/:id") {

      byMethod {

        def idPathToken = pathTokens.id as String

        get {
          persistenceService
              .getPerson(idPathToken)
              .subscribe { Document document ->
                render Jackson.json(document)
              }
        }

        put {

          def form = parse(Form)

          persistenceService.updatePerson(idPathToken, form.firstName as String, form.lastName as String, form.age as Integer).subscribe() { UpdateResult updateResult ->

            render updateResult
            //redirect "/person/$id"
          }
        }

        delete {

          persistenceService.removePerson(idPathToken).subscribe() { DeleteResult deleteResult ->

            redirect "/people"
          }
        }
      }
    }
  }
}
