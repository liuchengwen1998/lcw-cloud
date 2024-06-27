# 使用方法：
1、添加依赖：
    <dependency>
        <groupId>com.lcw</groupId>
        <artifactId>lcw-cloud-sentinel-starter</artifactId>
    </dependency>
2、对sentinel进行配置
spring.cloud.nacos.config.shared-dataids=lcw-sentinel.properties
3、管理后台进行限流配置
开发环境：http://192.168.100.187:8858/
测试环境：http://sentinel-test.1kmxc.com/
生产环境：http://sentinel.1kmxc.com/


lcw-sentinel.properties的配置
```agsl
#sentinel配置,todo 注意启动命令中加-Dproject.name=项目名
spring.cloud.sentinel.transport.dashboard=lcw-sentinel-svc:8858
# 流控规则
spring.cloud.sentinel.datasource.flow.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
spring.cloud.sentinel.datasource.flow.nacos.data-id=${spring.application.name}-flow-rules
spring.cloud.sentinel.datasource.flow.nacos.group-id=SENTINEL_GROUP
spring.cloud.sentinel.datasource.flow.nacos.rule-type=flow
spring.cloud.sentinel.datasource.flow.nacos.data-type=json
# 降级规则
spring.cloud.sentinel.datasource.degrade.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
spring.cloud.sentinel.datasource.degrade.nacos.data-id=${spring.application.name}-degrade-rules
spring.cloud.sentinel.datasource.degrade.nacos.group-id=SENTINEL_GROUP
spring.cloud.sentinel.datasource.degrade.nacos.rule-type=degrade
spring.cloud.sentinel.datasource.degrade.nacos.data-type=json
# 热点规则
spring.cloud.sentinel.datasource.param-flow.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
spring.cloud.sentinel.datasource.param-flow.nacos.data-id=${spring.application.name}-param-rules
spring.cloud.sentinel.datasource.param-flow.nacos.group-id=SENTINEL_GROUP
spring.cloud.sentinel.datasource.param-flow.nacos.rule-type=param-flow
spring.cloud.sentinel.datasource.param-flow.nacos.data-type=json
# 系统规则
spring.cloud.sentinel.datasource.system.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
spring.cloud.sentinel.datasource.system.nacos.data-id=${spring.application.name}-system-rules
spring.cloud.sentinel.datasource.system.nacos.group-id=SENTINEL_GROUP
spring.cloud.sentinel.datasource.system.nacos.rule-type=system
spring.cloud.sentinel.datasource.system.nacos.data-type=json
# 授权规则
spring.cloud.sentinel.datasource.authority.nacos.server-addr=${spring.cloud.nacos.config.server-addr}
spring.cloud.sentinel.datasource.authority.nacos.data-id=${spring.application.name}-authority-rules
spring.cloud.sentinel.datasource.authority.nacos.group-id=SENTINEL_GROUP
spring.cloud.sentinel.datasource.authority.nacos.rule-type=authority
spring.cloud.sentinel.datasource.authority.nacos.data-type=json
```