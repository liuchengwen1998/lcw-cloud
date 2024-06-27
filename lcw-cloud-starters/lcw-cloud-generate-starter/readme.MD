#### 自动代码生成操作步骤

##### 1.引入依赖，最好单独在
```xml
<!-- 父依赖，这样下面的相关依赖就可以不用输入版本号 -->
<parent>
    <artifactId>lcw-cloud-parent</artifactId>
    <groupId>com.lcw</groupId>
    <version>1.0.0</version>
</parent>


<!-- 如果不依赖父依赖的话就需要输入版本号 <version>1.0.0</version> -->
<dependency>
    <groupId>com.lcw</groupId>
    <artifactId>lcw-cloud-generate-starter</artifactId>
</dependency>
```


##### 2.配置 api.txt文件

> 默认文件会获取你的项目的跟路径下名称叫 api.txt，也可以通过参数自己设置 \
> 文件内容格式： 模块标识：二级系统名称：接口的中文名：接口的名称：请求路径：涉及到的表结构 
> - 模块编号：就是当前业务属于哪一模块，是在子系统下面的继续划分 
> - 二级系统名称: 在生成controller层出入参的前缀标识 
> - 接口的中文名: 接口的中文注释
> - 接口的名称: 接口的英文名称
> - 请求路径: 接口的请求的路径
> - 涉及到的表结构：相关的表结构，会生成mapper和xml文件

###### 具体例子：
```text
system,sysDictItem,字典项子项分页查询,getSysDictItemPage,/system/sysDict/getSysDictItemPage,omc_sys_dict_item
```

#### 3.新建一个class ，然后使用系统的main方法

```java

public static void main(String[] args) {
    CodeAutoGeneratorReq build = CodeAutoGeneratorReq.builder()
                    .dataBaseUrl("localhost") // 数据库的地址，默认为localhost
                    .dataBaseName("root") // 数据库的账号
                    .dataBasePwd("123456") // 数据库的密码
                    .dataBaseUsername("ygl-omc") // 数据库名
                    .module("omc") // 子系统名称，如果不需要则设置 ""
                    .model("system") // 模块名称，跟接口的第一个字段一个概念，主要用于xml和mapper的package路径
                    .generateTable(new String[]{"omc_sys_dict_item"}) // 需要创建的表结构，主要用于xml和mapper
                    .apiFilePath("具体的文件地址")// 默认获取当前项目下面的 api.txt文件
                    .tablePrefix("t_") // 如果有需要去掉的表前缀则使用
                    .fieldPrefix("f_") // 如果有需要去掉的字段前缀则使用
                    .build();
    CodeAutoGenerator generator = new CodeAutoGenerator(build);
    generator.generator();
}
```