package org.saur.initilaiser

import com.google.inject.AbstractModule
import org.saur.initilaiser.impl.GradleApplicationInitialiser


class InitialiserApiModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(Initialiser).to(GradleApplicationInitialiser)
    }
}
