package com.lcw.cloud.generate.writefile;

import com.lcw.cloud.generate.entity.ModuleInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 按模块输出相关内容
 *
 * @author yzhang
 * @since 1.0
 */
public class ModuleWriterFacade {
    private static final VelocityEngine ve = new VelocityEngine();

    private static final ControllerFileWriter controller = new ControllerFileWriter();
    private static final IOFileWriter io = new IOFileWriter();
    private static final DTOFileWriter dto = new DTOFileWriter();


    static {
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    public static void write(ModuleInfo moduleInfo) {
        Map<String, String> packages = getPackages(moduleInfo);
        VelocityContext context = getContext(moduleInfo, packages);
        controller.write(moduleInfo, ve, context, packages);
        io.write(moduleInfo, ve, context, packages);
        dto.write(moduleInfo, ve, context, packages);
        new VOFileWriter().write(moduleInfo, ve, context, packages);
        new ServiceFileWriter().write(moduleInfo, ve, context, packages);
        new ServiceImplFileWriter().write(moduleInfo, ve, context, packages);
        new AssemblerFileWriter().write(moduleInfo, ve, context, packages);
        new ContverterFileWriter().write(moduleInfo, ve, context, packages);
        new FeignFileWriter().write(moduleInfo, ve, context, packages);
    }

    public static VelocityContext getContext(ModuleInfo moduleInfo, Map<String, String> packages) {
        Map<String, Object> params = new HashMap<>();
        params.put("TIME", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        params.put("systemOrigin", moduleInfo.getSubSystemCode());
        params.put("system", StringUtils.capitalize(moduleInfo.getSubSystemCode()));
        params.put("module", StringUtils.capitalize(moduleInfo.getModuleName()));
        params.put("author", "由工具generator自动生成");
        params.put("pkgs", packages);
        VelocityContext ctx = new VelocityContext();
        ctx.put("cfg", params);
        ctx.put("moduleInfo", moduleInfo);
        return ctx;
    }


    private static final Map<String, String> getPackages(ModuleInfo moduleInfo) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(moduleInfo.getSubSystemCode())) {
            params.put("Controller", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".controller");
            params.put("Assembler", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".assembler");
            params.put("IoReq", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".domain.req");
            params.put("IoRes", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".domain.resp");
            params.put("Service", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".service");
            params.put("ServiceImpl", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".service.impl");
            params.put("Converter", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".converter");
            params.put("ServiceReqDTO", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".dto.req");
            params.put("ServiceResDTO", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".dto.resp");
            params.put("Feign", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".feign");
            params.put("PO", "com.lcw." + moduleInfo.getSubSystemCode() + "." + moduleInfo.getModuleName() + ".dao.po");
        } else {
            params.put("Controller", "com.lcw." + moduleInfo.getModuleName() + ".controller");
            params.put("Assembler", "com.lcw." + moduleInfo.getModuleName() + ".assembler");
            params.put("IoReq", "com.lcw." + moduleInfo.getModuleName() + ".domain.req");
            params.put("IoRes", "com.lcw." + moduleInfo.getModuleName() + ".domain.resp");
            params.put("Service", "com.lcw." + moduleInfo.getModuleName() + ".service");
            params.put("ServiceImpl", "com.lcw." + moduleInfo.getModuleName() + ".service.impl");
            params.put("Converter", "com.lcw." + moduleInfo.getModuleName() + ".converter");
            params.put("ServiceReqDTO", "com.lcw." + moduleInfo.getModuleName() + ".dto.req");
            params.put("ServiceResDTO", "com.lcw." + moduleInfo.getModuleName() + ".dto.resp");
            params.put("Feign", "com.lcw." + moduleInfo.getModuleName() + ".feign");
            params.put("PO", "com.lcw." + moduleInfo.getModuleName() + ".dao.po");
        }
        return params;
    }

}
