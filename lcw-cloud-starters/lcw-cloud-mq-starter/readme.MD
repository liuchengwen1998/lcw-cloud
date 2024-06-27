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
    <artifactId>lcw-cloud-mq-starter</artifactId>
</dependency>
```

##### 2.配置相关的配置文件
```yaml
ygl:
  rocketmq: # rocketmq配置，支持多个produce
    list:
        - key: default # 默认发送选择项，一般配置成公司内部使用的
          accessKey: LTAIvpX2qUaO60L3
          secretKey: 6uNFbgF8FDtLPJMaNwKofOBW4QmCqy
          nameSrvAddr: http://MQ_INST_1866335219679020_Bay4IFyQ.mq-internet-access.mq-internet.aliyuncs.com:80
          endpoint: ons.mq-internet-access.aliyuncs.com
          instanceId: MQ_INST_1866335219679020_Bay4IFyQ
```

##### 3.使用生产者
```java
    // 使用特定mq的发送消息
    ProducerClientContext.get("key").buildDelayMessage("");

    // 使用默认mq的发送消息
    ProducerClientContext.getDefault().buildDelayMessage("");

```


##### 4.使用消费者

```java
    // 1.使用默认的mq接收消息
    @RocketListener(topic = FlyMqConstant.Topic.TOPIC_FLY_POINT_PROD,
            groupID = FlyMqConstant.Group.GROUP_FLY_POINT_PROD)
    public class LdProdMessageConsumer {}

    @MessageListener(tag = FlyMqConstant.Tag.TAG_FLY_POINT + "||" + FlyMqConstant.Tag.TAG_FLY_ALARM)
    public void notify(Message message) {}




    // 2.使用特定的mq接收消息
    @RocketListener(key= "mykey",topic = FlyMqConstant.Topic.TOPIC_FLY_POINT_PROD,
            groupID = FlyMqConstant.Group.GROUP_FLY_POINT_PROD)
    public class LdProdMessageConsumer {}

    @MessageListener(tag = FlyMqConstant.Tag.TAG_FLY_POINT + "||" + FlyMqConstant.Tag.TAG_FLY_ALARM)
    public void notify(Message message) {}

```