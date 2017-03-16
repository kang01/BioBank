### 诡异问题集锦 -- zhu yu
1. liquibase 在使用 jhipster-entity-audit 后，数据库初始化失败，报错Connection is closed。  
原因：Hibernate 5.2.8 和 liquibase 3.5.0这两个版本有冲突，导致liquibase执行时数据库连接丢失。将Hibernate降级至5.2.4问题解决。


### 诡异问题集锦 -- geng luying


### 诡异问题集锦 -- gao kangkang
