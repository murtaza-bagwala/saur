package org.saur
import com.google.inject.AbstractModule
import org.saur.generator.CreatorApiModule
import org.saur.initilaiser.InitialiserApiModule

class SaurModule extends AbstractModule{

    SaurModule() {
    }

    @Override
    protected void configure() {
        this.install(new CreatorApiModule())
        this.install(new InitialiserApiModule())
    }


}
