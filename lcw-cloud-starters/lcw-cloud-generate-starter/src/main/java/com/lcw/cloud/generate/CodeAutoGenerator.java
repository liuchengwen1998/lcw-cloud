package com.lcw.cloud.generate;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.generate.entity.*;
import com.lcw.cloud.generate.util.HumpUtil;
import com.lcw.cloud.generate.writefile.ModuleWriterFacade;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author yzhang
 * @since 2022/8/2
 * 业务自动代码生成工具
 */
public class CodeAutoGenerator {

    private CodeAutoGeneratorReq req;

    // 输出路径
    private final String outPutPath = System.getProperty("user.dir") + "/build";

    private final String inputPath = System.getProperty("user.dir") + "/api.txt";


    public CodeAutoGenerator(CodeAutoGeneratorReq req) {
        this.req = req;
    }

    public static void main(String[] args) {
        CodeAutoGeneratorReq build = CodeAutoGeneratorReq.builder()
                .dataBaseUrl("localhost") // 数据库的地址，默认为localhost
                .dataBaseName("lcw-omc") // 数据库名
                .dataBasePwd("qwer1234@$") // 数据库的密码
                .dataBaseUsername("root") // 数据库的账号
                .module("") // 子系统名称
                .model("system") // 模块名称，跟接口的第一个字段一个概念，主要用于xml和mapper的package路径
                .generateTable(new String[]{"t_sys_dict_item"}) // 需要创建的表结构，主要用于xml和mapper
                .tablePrefix("t_")
                .fieldPrefix("f_")
                .build();
        CodeAutoGenerator generator = new CodeAutoGenerator(build);
        generator.generator();
    }

    public void generator() {
        // 删除目录
        removeGeneratorDir();
        // 判断是否需要生成service和controller层
        if (Objects.isNull(req.getNeedGeneratorService()) || req.getNeedGeneratorService()) {
            // 生成controller 和 service层
            generatorServiceAndControllerCode();
        }
        // 生成基础类
        generatorBaseCode();
    }

    private void removeGeneratorDir() {
        try {
            //定义代码生成后的存放路径
            final String outPutPath = System.getProperty("user.dir") + "/build";
            System.out.println(outPutPath);
            // 删除 目录
            if (System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                String cmd = "cmd /c rd/s/q " + System.getProperty("user.dir") + "\\build";
                Runtime.getRuntime().exec(cmd);
            } else {
                Runtime.getRuntime().exec("rm -rf " + outPutPath);
            }
        } catch (Exception ex) {
            LogFactory.bizErr("删除文件夹" + outPutPath + "失败", ex);
        }

    }


    /**
     * 生成service层和controller层的代码
     */
    private void generatorServiceAndControllerCode() {
        // 读取文件
        Map<String, ModuleInfo> modules = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(StringUtils.isNotBlank(req.getApiFilePath()) ? req.getApiFilePath() : inputPath))) {
            String line;
            while (StringUtils.isNotBlank(line = reader.readLine())) {
                String[] lines = line.split(",");
                ApiInfo apiInfo = ApiInfo.of(lines[0], lines[1], lines[2], lines[3], lines[4]);
                apiInfo.setSysCode(req.getModule());
                apiInfo.setTableName(HumpUtil.lineToBigHump(lines[5]));
                if (modules.containsKey(apiInfo.getModuleName())) {
                    Map<String, List<ApiInfo>> apis = modules.get(apiInfo.getModuleName()).getApis();
                    if (!apis.containsKey(apiInfo.getsModuleName())) {
                        apis.put(apiInfo.getsModuleName(), new ArrayList<>());
                    }
                    apis.get(apiInfo.getsModuleName()).add(apiInfo);
                } else {
                    List<ApiInfo> apiInfos = new ArrayList<>();
                    apiInfos.add(apiInfo);
                    ModuleInfo moduleInfo = new ModuleInfo();
                    moduleInfo.setSubSystemCode(req.getModule());
                    moduleInfo.setModuleName(apiInfo.getModuleName());
                    moduleInfo.getApis().put(apiInfo.getsModuleName(), apiInfos);
                    modules.put(moduleInfo.getModuleName(), moduleInfo);
                }
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        modules.forEach((k, v) -> {
            ModuleWriterFacade.write(v);
        });
    }


    /**
     * 生成基础的 mapper和xml代码
     */
    private void generatorBaseCode() {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outPutPath);//这里写你自己的java目录
        gc.setFileOverride(true);//是否覆盖
        gc.setOpen(false);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);//二级缓存
        gc.setBaseResultMap(true);//ResultMap
        gc.setBaseColumnList(true);//columList
        gc.setAuthor("Mybatis-Generator");
        gc.setDateType(DateType.ONLY_DATE);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(req.getDataBaseUsername());
        dsc.setPassword(req.getDataBasePwd());
        dsc.setUrl("jdbc:mysql://" + req.getDataBaseUrl() + ":3306/" + req.getDataBaseName() + "?serverTimezone=Asia/Shanghai&characterEncoding=utf-8&useSSL=false&noAccessToProcedureBodies=true&allowMultiQueries=true&autoReconnect=true");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略d
        strategy.setInclude(req.getGenerateTable()); // 需要生成的表
        // 生成 RestController 风格
        strategy.setRestControllerStyle(true);
        if (StringUtils.isNotBlank(req.getTablePrefix())) {
            strategy.setTablePrefix(req.getTablePrefix());
        }
        if (StringUtils.isNotBlank(req.getFieldPrefix())) {
            strategy.setFieldPrefix(req.getFieldPrefix());
        }
        mpg.setStrategy(strategy);

        // 包配置
        // 注意不同的模块生成时要修改对应模块包名
        PackageConfig pc = new PackageConfig();
        pc.setParent(null);
        if (StringUtils.isNotBlank(req.getModule())) {
            pc.setEntity("com.lcw." + req.getModule() + "." + req.getModel() + ".dao.po");
            pc.setMapper("com.lcw." + req.getModule() + "." + req.getModel() + ".dao");
        } else {
            pc.setEntity("com.lcw." + req.getModel() + ".dao.po");
            pc.setMapper("com.lcw." + req.getModel() + ".dao");
        }

        // 这个xml文件生成出来之后需要拷贝到resources/mapper/文件下
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                map.put("TIME", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                List<String> baseColumn = new ArrayList<>();
                baseColumn.add("id");
                baseColumn.add("createTime");
                baseColumn.add("updateTime");
                if (StringUtils.isNotBlank(req.getModule())) {
                    map.put("converterPackage", "com.lcw." + req.getModule() + "." + req.getModel() + ".service.converter");
                    map.put("assemblerPackage", "com.lcw." + req.getModule() + "." + req.getModel() + ".assembler");
                } else {
                    map.put("converterPackage", "com.lcw." + req.getModel() + ".service.converter");
                    map.put("assemblerPackage", "com.lcw." + req.getModel() + ".assembler");
                }
                map.put("baseColumn", baseColumn);
                this.setMap(map);
            }

        };

        List<FileOutConfig> fileOutConfigList = new ArrayList<>(6);
        fileOutConfigList.add(new EntityPO("generator/entityPO.java.vm", pc));
        fileOutConfigList.add(new FieldsJson("generator/fields.json.vm", pc));
        cfg.setFileOutConfigList(fileOutConfigList);
        mpg.setCfg(cfg);

        TemplateConfig tc = new TemplateConfig();
        tc.setMapper("/generator/mapper.java.vm");
        tc.setXml("/generator/mapper.xml.vm");
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity(null);
        tc.setController(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置
        System.err.println(mpg.getCfg().getMap().get("abc"));

    }
}
