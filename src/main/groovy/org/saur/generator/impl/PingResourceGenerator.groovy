package org.saur.generator.impl
import com.google.inject.Inject
import org.saur.generator.AbstractCodeGenerator
import org.saur.model.Node

import static org.saur.Constants.PACKAGE
import static org.saur.Constants.ROOT_ROUTER_KEY

class PingResourceGenerator  extends AbstractCodeGenerator{
    static final String PING_RESOURCE_PATH = '${packageName}/${rootRouter}.groovy'
    private Map<String, String> binding

    @Inject
    PingResourceGenerator() {
        super(PING_RESOURCE_PATH)
        this.binding = new HashMap<>()
    }

    @Override
    public File createFile( File projRootDir, String fileName, String packageName) {
        binding.putAll([("${PACKAGE}".toString()) : packageName])
        binding.putAll([("${ROOT_ROUTER_KEY}".toString()) : fileName])
        def  metaRouterClass = super.generateFile(projRootDir, binding)
        return metaRouterClass
    }

    @Override
    public String createCode( String metaRouterTemplate ) {
        return super.generateCode(metaRouterTemplate, binding)
    }

    @Override
    protected void createBindings(Node node) {

    }
}
