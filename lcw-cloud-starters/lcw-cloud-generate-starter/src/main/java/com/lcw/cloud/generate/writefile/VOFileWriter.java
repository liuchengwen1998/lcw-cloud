package com.lcw.cloud.generate.writefile;

import com.lcw.cloud.generate.entity.ModuleInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * vo对象生成类
 *
 * @author yzhang
 * @since 1.0
 */
public class VOFileWriter extends BaseFileWriter {

    @Override
    protected void write(ModuleInfo moduleInfo, VelocityEngine ve, VelocityContext ctx,
                         Map<String, String> packages) {
        moduleInfo.getApis().forEach((k, v) -> {
            v.forEach(api -> {
                if (!api.getVoName().equalsIgnoreCase("String")) {
                    ctx.put("className", api.noPrefixMethodName + api.getMethodPrefix() + "VO");
                    write(moduleInfo, k, ve, ctx, packages, getFileName("IoRes", packages,
                            StringUtils.capitalize(moduleInfo.getSubSystemCode()) + api.noPrefixMethodName
                                    + api.getMethodPrefix() + "VO"));
                }
            });
        });
    }

    private String getFileName(String key, Map<String, String> packages, String fileName) {
        return packages.get(key).replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + fileName + ".java";
    }

    @Override
    protected Set<String> getImports(ModuleInfo moduleInfo, String className,
                                     Map<String, String> packages) {
        return null;
    }

    @Override
    public String getTemplate(String subSystemCode) {
        if (StringUtils.isNotBlank(subSystemCode)) {
            return "api/vo.java.vm";
        } else {
            return "api2/vo.java.vm";
        }

    }
}
