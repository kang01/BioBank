### 诡异问题集锦 -- zhu yu
1. liquibase 在使用 jhipster-entity-audit 后，数据库初始化失败，报错Connection is closed。  
原因：Hibernate 5.2.8 和 liquibase 3.5.0这两个版本有冲突，导致liquibase执行时数据库连接丢失。将Hibernate降级至5.2.4问题解决。

2. angular-datatables 如何实现Ajax数据请求  
这几个属性必须配置上。
```javascript 1.8
vm.dtOptions = DTOptionsBuilder.fromSource('api/res/tranships')
            .withOption('sServerMethod','POST')
            .withOption('processing',true)
            .withOption('serverSide',true)
            // .withOption('sAjaxSource', 'api/res/tranships')  // 如果不注释这个属性，下面fnServerData中的aoData将会使用1.9.*的格式返回数据。
            .withOption('fnServerData', function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                // aoData是一个，name和value的键值对数组，所以要变成object才能被后端接收
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                
                // 这里要使用系统中的service使用$http访问数据，否则将会返回403错误
                TransportRecordService.getJqDataTableValues(data, oSettings).then(function (res){
                    var json = res.data;
                    var error = json.error || json.sError;
                    if ( error ) {
                        jqDt._fnLog( oSettings, 0, error );
                    }
                    oSettings.json = json;
                    fnCallback( json );
                }).catch(function(res){
                    console.log(res);

                    var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );

                    if ( $.inArray( true, ret ) === -1 ) {
                        if ( error == "parsererror" ) {
                            jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
                        }
                        else if ( res.readyState === 4 ) {
                            jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
                        }
                    }
                    
                    // 关闭Processing文字显示
                    jqDt._fnProcessingDisplay( oSettings, false );
                });
            })
```
后端需要引入 spring-data-jpa-datatables 组件
```xml
<dependency>
    <groupId>com.github.darrachequesne</groupId>
    <artifactId>spring-data-jpa-datatables</artifactId>
    <version>4.0</version>
</dependency>
```
```java
// Controller 定义时要注意返回的JsonView必须写对
@JsonView(DataTablesOutput.View.class)
@RequestMapping(value = "/res/tranships", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
public DataTablesOutput<TranshipResponse> getPageTranship(@RequestBody DataTablesInput input) {
    
    // 注意input里面都是字符串类型的搜索条件，如果对数据有要求应该在findAll之前进行数据检查
    
    return transhipService.findAllTranship(input);
}
```

3. 减少jQuery Datatables Column Filter中访问服务器的次数，在如下位置配置属性
```javascript 1.8
.withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50,
                    iFilterLength:3   // 最少输入3个字符才会开始过滤
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true,
                    iFilterLength:3
                },
```
4. ORA-01400 错误。 在Oracle中 "" == NULL，所以不能用空字符串作为不为空字段的默认值。EntityUtil.avoidFieldValueNull()这个工具方法可以将一个对象中的所有null和""转换成非空数据。目前只检查String和Integer的字段，其他字段不转换。
 
5. Oracle 12C之后支持 auto-increment 的字段，所以在生命实体时不用指定GenerationType.SEQUENCE，可以指定GenerationType.IDENTITY。可以规避id重复的错误。


### 诡异问题集锦 -- geng luying


### 诡异问题集锦 -- gao kangkang


### 工程打包 & 部署
0. 准备测试数据库
1. 修改 pom.xml 和 application-dev.yml 中的数据库连接。
2. mvnw 启动工程构建数据库
3. mvnw -DskipTests package 打包工程
4. scp上传到10.24.209.11服务器
5. 找到正在运行的BBIS  
ps -aux | grep "biob"
6. 结束进程  
kill 23173  
7. 启动新的BBIS  
nohup java -jar ~/biobank/bbis-0.0.1/bio-bank-0.0.1-SNAPSHOT.war >~/biobank/bbis-0.0.1/log.file 2>&1 &

