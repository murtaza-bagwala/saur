package org.saur

import com.google.inject.Guice
import com.google.inject.Injector
import groovy.transform.CompileStatic
import org.saur.initilaiser.Initialiser
import org.saur.initilaiser.impl.GradleApplicationInitialiser

@CompileStatic
class Saur {
    public static void main(String[] args ) {
        Injector saurInjector = Guice.createInjector(new SaurModule())
        Initialiser gradleAppInitialiser = saurInjector.getInstance(GradleApplicationInitialiser)
        def rootDir = gradleAppInitialiser.prepareEnvironment()
         gradleAppInitialiser.writeAPI()
        gradleAppInitialiser.writeRootRouterClass(rootDir)
        println("Swagger File Created")
    }
}
