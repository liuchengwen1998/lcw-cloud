#### Redis使用

##### 1.引入依赖，最好单独在
```pom
<!-- 父依赖，这样下面的相关依赖就可以不用输入版本号 -->
<parent>
    <artifactId>lcw-cloud-parent</artifactId>
    <groupId>com.lcw</groupId>
    <version>1.0.0</version>
</parent>


<!-- 如果不依赖父依赖的话就需要输入版本号 <version>1.0.0</version> -->
<dependency>
    <groupId>com.lcw</groupId>
    <artifactId>lcw-cloud-redis-starter</artifactId>
</dependency>
```

##### 2.配置相关的配置文件
```yaml
spring:
  redis:
    database: 1
    host: localhost       # Redis服务器地址
    port: 6379              # Redis服务器连接端口
    password: ygl              # Redis服务器连接密码（默认为空）
```

##### 3.使用redis的工具RedisUtils来操作redis
```java
    com.ygl.cloud.redis.util.RedisUtils.xxx();
```

##### 4.redis使用规范
- 数字字典存储规范 \
  omc: ----------------------------> 子系统标识 \
      dict: ----------------------->数据字典特殊标识 \
        [dict-entry]---------> key: 字典项编号，value: 字典项及子项内容 

  key举例：omc:dict:gender

- 配置信息存储规范
  omc: ----------------------------> 子系统标识 \
     config: ----------------------->配置项特殊标识 \
        [configCode]---------> key: 配置项编号，value: 配置内容

  key举例：omc:config:resetPwd

- 会话存储规范
  omc: ----------------------------> 子系统标识 \
     session: ----------------------->会话特殊标识 \
        [tokenId]---------> key: 会话的tokenId，value: 会话的相关信息 

  key举例：omc:session:837996b262d94b00-91aa3ed63e5e1143



