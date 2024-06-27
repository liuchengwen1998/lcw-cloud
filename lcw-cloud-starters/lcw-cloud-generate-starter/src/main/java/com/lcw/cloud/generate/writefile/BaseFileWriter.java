package com.lcw.cloud.generate.writefile;

import com.lcw.cloud.generate.entity.ModuleInfo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * @author yzhang
 * @since 1.0
 */
public abstract class BaseFileWriter {

    protected abstract void write(ModuleInfo moduleInfo, VelocityEngine ve, VelocityContext ctx, Map<String, String> packages);

    protected void write(ModuleInfo moduleInfo, String className, VelocityEngine ve, VelocityContext ctx, Map<String, String> packages, String fileName) {
        Template template = ve.getTemplate(getTemplate(moduleInfo.getSubSystemCode()));
        ctx.put("imports", getImports(moduleInfo, className, packages));
        final String outPutPath = System.getProperty("user.dir") + "/build/";
        File saveFile = new File(outPutPath + fileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        //因为模板整合的时候，需要提供一个Writer，所以创建一个Writer
        //创建一个缓冲流
        try (FileOutputStream outStream = new FileOutputStream(saveFile);
             OutputStreamWriter writer = new OutputStreamWriter(outStream);
             BufferedWriter bufferWriter = new BufferedWriter(writer)) {

            template.merge(ctx, bufferWriter);
            bufferWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    protected abstract Set<String> getImports(ModuleInfo moduleInfo, String className, Map<String, String> packages);

    public abstract String getTemplate(String subSystemCode);


}
