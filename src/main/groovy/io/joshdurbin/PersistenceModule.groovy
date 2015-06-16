package io.joshdurbin

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import groovy.transform.CompileStatic

@CompileStatic
class PersistenceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(RxMongoLabPersistenceService).in(Scopes.SINGLETON)
  }
}
