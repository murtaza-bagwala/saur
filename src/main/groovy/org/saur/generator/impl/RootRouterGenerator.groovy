package org.saur.generator.impl
import com.google.inject.Inject
import org.saur.generator.AbstractCodeGenerator
import org.saur.model.Node
import org.saur.model.Router

import static org.saur.Constants.PACKAGE
import static org.saur.Constants.ROOT_ROUTER_KEY

class RootRouterGenerator extends AbstractCodeGenerator {
    static final String ROOT_ROUTER_PATH = '${packageName}/${rootRouter}.groovy'
    Map<String, String> binding
    protected final List<Router> modelList

    @Inject
    RootRouterGenerator( ) {
        super(ROOT_ROUTER_PATH)
        binding = new HashMap<>()
        modelList = new ArrayList<>();
    }

    @Override
    public File createFile(File projRootDir, String fileName, String packageName) {
        binding.putAll([("${PACKAGE}".toString()) : packageName])
        binding.putAll([("${ROOT_ROUTER_KEY}".toString()) : fileName])
        def metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( String metaRouterTemplate ) {
        return super.generateCode(metaRouterTemplate, binding)
    }

    @Override
    protected void createBindings(Node node) {
        def router = new Router("path": node.getPathName(), "pathRouter": node
                .hasChildren() ? node.getPathName().replace("/", "") +
                "Router" : node.getPathName().replace("/", "") + "Resource");
        this.modelList.add(router)
        this.binding.putAll(["attachSubRouter" : this.modelList.join("\n")])
    }

    public void resetBindings() {
        this.binding.clear();
        this.modelList.clear();
    }


}
