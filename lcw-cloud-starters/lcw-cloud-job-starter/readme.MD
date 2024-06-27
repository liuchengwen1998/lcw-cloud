#### AliRocketMQ使用

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
    <artifactId>lcw-cloud-job-starter</artifactId>
</dependency>
```

##### 2.配置相关的配置文件
```yaml
xxl:
  job:
    admin:
      addresses: http://ygl-job-admin-svc:8080/admin
    accessToken:
    executor:
      address:
      logPath: /data/logs/xxl-job/jobHandler #日志存储位置
      logRetentionDays: 30
      ip:
      port: 9999
```

##### 3.使用xxljob

```java
// 1.先创建一个任务
@Component
public class JobClass {

    @XxlJob("jobName")
    public void run() {

    }
}

// 然后在xxl-admin页面上面添加一个任务
```
