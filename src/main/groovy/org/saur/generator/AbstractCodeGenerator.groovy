package org.saur.generator
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import org.saur.model.Node

import java.nio.file.Paths

abstract class AbstractCodeGenerator {

    private Template filePathTemplate
    private SimpleTemplateEngine simpleTemplateEngine

    AbstractCodeGenerator(String codeFilePath) {
        simpleTemplateEngine = new SimpleTemplateEngine()
        filePathTemplate = simpleTemplateEngine.createTemplate(codeFilePath)
    }

    protected File generateFile(File projRootDir, Map<String, String> binding){
        def generatedFilePath = filePathTemplate.make(binding).toString()
        generatedFilePath = Paths.get(generatedFilePath).toFile().toString()
        def generatedFile = new File(projRootDir, generatedFilePath)
        generatedFile.getParentFile().mkdirs()
        generatedFile.createNewFile()
        return generatedFile
    }

    protected String generateCode(String template,  Map<String, String> binding){
        return simpleTemplateEngine.createTemplate(template).make(binding).toString()

    }

    abstract protected File createFile(File projRootDir, String fileName, String packageName);
    abstract protected String createCode(String metaRouterTemplate);
    abstract protected void createBindings(Node node);

    protected String groupPackage(String groupId){
        return groupId.replace(".", "/")
    }
}
