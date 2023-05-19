package com.whz.mybatis.generator.config;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyHolder;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @Author 盖伦
 * @Date 2023/5/18
 */
public class JavaQueryModelGeneratorConfiguration extends PropertyHolder {

    private String targetPackage;

    private String targetProject;

    /**
     *
     */
    public JavaQueryModelGeneratorConfiguration() {
        super();
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("javaQueryModelGenerator");

        if (targetPackage != null) {
            answer.addAttribute(new Attribute("targetPackage", targetPackage));
        }

        if (targetProject != null) {
            answer.addAttribute(new Attribute("targetProject", targetProject));
        }

        addPropertyXmlElements(answer);

        return answer;
    }

    public void validate(List<String> errors, String contextId) {
        if (!stringHasValue(targetProject)) {
            errors.add(getString("ValidationError.0", contextId));
        }

        if (!stringHasValue(targetPackage)) {
            errors.add(getString("ValidationError.12", "JavaModelGenerator", contextId));
        }
    }
}