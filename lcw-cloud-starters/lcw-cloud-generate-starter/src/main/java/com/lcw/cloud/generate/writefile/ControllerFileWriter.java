package com.lcw.cloud.generate.writefile;

import com.lcw.cloud.generate.entity.ModuleInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * 控制器文件生成类
 *
 * @author yzhang
 * @since 1.0
 */
public class ControllerFileWriter extends BaseFileWriter {

    @Override
    protected Set<String> getImports(ModuleInfo moduleInfo, String className, Map<String, String> packages) {
        Set<String> imports = new HashSet<>();
        String sysCode = StringUtils.capitalize(moduleInfo.getSubSystemCode());
        moduleInfo.getApis().get(className).forEach(apiInfo -> {
            imports.add(packages.get("IoReq") + "." + sysCode + apiInfo.getNoPrefixMethodName() + apiInfo.getMethodPrefix() + "IO");
            imports.add(packages.get("ServiceReqDTO") + "." + sysCode + apiInfo.getNoPrefixMethodName() + apiInfo.getMethodPrefix() + "ReqDTO");
            if (!StringUtils.equalsIgnoreCase("String", apiInfo.getVoName())) {
                imports.add(packages.get("ServiceResDTO") + "." + sysCode + apiInfo.getNoPrefixMethodName() + apiInfo.getMethodPrefix() + "ResDTO");
                imports.add(packages.get("IoRes") + "." + sysCode + apiInfo.getNoPrefixMethodName() + apiInfo.getMethodPrefix() + "VO");
            }
            if (apiInfo.getVoName().contains("Page")) {
                imports.add("java.util.List");
                imports.add("com.lcw.cloud.core.entity.Pagination");
            }
            if (apiInfo.getVoName().contains("List")) {
                imports.add("java.util.List");
            }
        });
        return imports;
    }

    @Override
    protected void write(ModuleInfo moduleInfo, VelocityEngine ve, VelocityContext ctx, Map<String, String> packages) {
        moduleInfo.getApis().forEach((k, v) -> {
            ctx.put("apis", v);
            ctx.put("className", StringUtils.capitalize(k));
            write(moduleInfo, k, ve, ctx, packages,
                    getFileName(packages, StringUtils.capitalize(moduleInfo.getSubSystemCode()) + StringUtils.capitalize(k) + "Controller"));
        });
    }

    private String getFileName(Map<String, String> packages, String fileName) {
        return packages.get("Controller").replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + fileName + ".java";
    }

    @Override
    public String getTemplate(String subSystemCode) {
        if (StringUtils.isNotBlank(subSystemCode)) {
            return "api/controller.java.vm";
        } else {
            return "api2/controller.java.vm";
        }
    }

}
