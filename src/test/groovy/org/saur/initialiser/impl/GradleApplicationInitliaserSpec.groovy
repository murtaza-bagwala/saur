package org.saur.initialiser.impl
import com.google.inject.Guice
import com.google.inject.Injector
import groovy.text.SimpleTemplateEngine
import org.saur.SaurModule
import org.saur.generator.impl.RootRouterGenerator
import org.saur.initilaiser.Initialiser
import org.saur.initilaiser.impl.GradleApplicationInitialiser
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Paths

import static org.saur.Constants.DEFAULT_PACKAGE
import static org.saur.Constants.DEFAULT_ROOT_ROUTER

class GradleApplicationInitliaserSpec extends Specification {
    @Shared
    Initialiser gradleAppInitialiser


    File rootDirectory

    @Shared
    private Injector injector

    private String groupId

    def setupSpec(){
        injector = Guice.createInjector(new SaurModule())
        gradleAppInitialiser = injector.getInstance(GradleApplicationInitialiser)

    }

    def setup(){
        rootDirectory = gradleAppInitialiser.prepareEnvironment()
    }

    def "prepare environment should download the gradle wrapper"(){
        when:
            rootDirectory = gradleAppInitialiser.prepareEnvironment()
        then:
        assert  rootDirectory.isDirectory() : "Root Directory not created"
    }

    def "writeRootRouterFile should be created with given servlet names"(){
        when:
            gradleAppInitialiser.writeAPI()
            gradleAppInitialiser.writeRootRouterClass(rootDirectory)
        then:
            assert  rootDirectory.isDirectory() : "Root Directory not created"
            def engine = new SimpleTemplateEngine()
            def filePath = engine.createTemplate(RootRouterGenerator.ROOT_ROUTER_PATH).make(["packageName" : DEFAULT_PACKAGE, "rootRouter" : DEFAULT_ROOT_ROUTER]).toString()
            def rootRouter = new File(rootDirectory, Paths.get(filePath).toFile().toString())
            assert rootRouter.exists() :" MainRouter.groovy does not exist"
    }

    def cleanup(){
      rootDirectory.deleteDir()
    }
}
