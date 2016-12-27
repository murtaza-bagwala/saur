package org.saur.generator.impl
import com.google.inject.Inject
import org.apache.commons.lang3.StringUtils
import org.saur.generator.AbstractCodeGenerator
import org.saur.model.Node
import org.saur.model.Router

import static org.saur.Constants.BLANK_SPACE
import static org.saur.Constants.PACKAGE
import static org.saur.Constants.REGEX
import static org.saur.Constants.RESOURCE_PACKAGE
import static org.saur.Constants.ROOT_ROUTER_KEY
import static org.saur.Constants.ROUTER_PACKAGE

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
                .hasChildren() ? StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE)) + StringUtils
                .capitalize(ROUTER_PACKAGE) : StringUtils.capitalize(node.getPathName().replace(REGEX, BLANK_SPACE))
                + StringUtils
                .capitalize(RESOURCE_PACKAGE));
        this.modelList.add(router)
        this.binding.putAll(["attachSubRouter": this.modelList.join("\n")])
    }

    public void resetBindings() {
        this.binding.clear();
        this.modelList.clear();
    }

}
