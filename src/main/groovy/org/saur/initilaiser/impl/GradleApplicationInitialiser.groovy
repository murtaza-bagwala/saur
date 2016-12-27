package org.saur.initilaiser.impl
import com.google.inject.Inject
import io.swagger.models.Path
import io.swagger.models.Swagger
import io.swagger.parser.SwaggerParser
import org.apache.commons.lang3.StringUtils
import org.saur.generator.impl.PingResourceGenerator
import org.saur.generator.impl.RootRouterGenerator
import org.saur.initilaiser.Initialiser
import org.saur.initilaiser.TreeParser
import org.saur.model.Node

import java.nio.file.Paths

import static org.saur.Constants.*

class GradleApplicationInitialiser implements Initialiser {
    private final RootRouterGenerator  rootRouterCreator
    private final PingResourceGenerator pingResourceCreator
    private final String ROOT_ROUTER_CLASS_TEMPLATE = "rootRouterClassTemplate.txt"
    private final String PING_RESOURCE_CLASS_TEMPLATE = "pingResourceClassTemplate.txt"
    private final String rootRouterTemplate
    private final String pingResourceTemplate
    private Swagger swagger
    private HashMap<String, Path> paths
    private File projectRootDir;

    @Inject
    GradleApplicationInitialiser(RootRouterGenerator rootRouterCreator,
                                  PingResourceGenerator pingResourceCreator) {

        this.rootRouterCreator = rootRouterCreator
        this.pingResourceCreator = pingResourceCreator
        ClassLoader classLoader = getClass().getClassLoader();
        rootRouterTemplate = getResourceFileFromLoader(classLoader, ROOT_ROUTER_CLASS_TEMPLATE)
        pingResourceTemplate = getResourceFileFromLoader(classLoader, PING_RESOURCE_CLASS_TEMPLATE)
    }

    private String getResourceFileFromLoader(ClassLoader classLoader, String fileName){
        def resourceStrem = classLoader.getResourceAsStream(fileName)
        return resourceStrem.text
    }

    @Override
    File prepareEnvironment() {
        String currentPath = System.getProperty("user.dir")
        File projectRootDirectory = new File(currentPath, "swagger")
        if ( projectRootDirectory.exists() ) {
            projectRootDirectory.deleteDir()
        }
        projectRootDirectory.mkdir()
        return projectRootDirectory
    }

    @Override
    void writeRootRouterClass(File projectRootDir) {
        List <Node> nodeList = TreeParser.parseTree(paths.keySet().asList()).values().asList();
        List<Node> rootNodes = nodeList.findAll {node -> node.path.equals(node.pathName)}
        this.projectRootDir = projectRootDir
        def routerClassFile = this.rootRouterCreator.createFile(projectRootDir, DEFAULT_ROOT_ROUTER, ROUTER_PACKAGE)
        rootNodes.forEach {node -> this.rootRouterCreator.createBindings(node)}
        def rootRouterTemplate = this.rootRouterCreator.createCode(rootRouterTemplate)
        routerClassFile.write(rootRouterTemplate)
        writeMetaRouterClass(rootNodes)
    }

    void writeMetaRouterClass(List<Node> rootNodes) {
        this.rootRouterCreator.resetBindings()
        rootNodes.forEach { node ->
            String fileName;
            String packageName;
            if (node.hasChildren()) {
                fileName = StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE)) + StringUtils
                        .capitalize(ROUTER_PACKAGE)
                packageName = ROUTER_PACKAGE;
            } else {
                fileName = StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE)) + StringUtils
                        .capitalize(RESOURCE_PACKAGE)
                packageName = RESOURCE_PACKAGE;
            }
            def routerClassFile = this.rootRouterCreator.createFile(projectRootDir, fileName, packageName)
            List<Node> childNodes = node.getChildren().values().asList()
            def metaRouterTemplate
            if (childNodes.isEmpty()) {
                metaRouterTemplate = this.rootRouterCreator.createCode(pingResourceTemplate)
            } else {
                childNodes.forEach { childNode -> this.rootRouterCreator.createBindings(childNode) }
                metaRouterTemplate = this.rootRouterCreator.createCode(rootRouterTemplate)
            }

            routerClassFile.write(metaRouterTemplate)
            writeMetaRouterClass(childNodes)

        }
    }

    @Override
    void writePingResourceClass( File projectRootDir ) {
        def pingResourceClassFile = this.pingResourceCreator.createFile(projectRootDir)
        def pingResourceTemplate = this.pingResourceCreator.createCode(pingResourceTemplate)
        pingResourceClassFile.write(pingResourceTemplate)
    }

    @Override
    void writeAPI() {
        String swaggerFilePath = Paths.get(System.properties['user.dir'], "swagger.json").toString()
        print swaggerFilePath
        swagger = new SwaggerParser().read(swaggerFilePath);
        paths = swagger.getPaths();
    }
}
