package org.saur.generator
import com.google.inject.AbstractModule
import org.saur.generator.impl.PingResourceGenerator
import org.saur.generator.impl.RootRouterGenerator

class CreatorApiModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(PingResourceGenerator)
        bind(RootRouterGenerator)
    }
}
