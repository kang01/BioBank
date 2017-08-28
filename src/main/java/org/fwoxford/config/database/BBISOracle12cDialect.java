package org.fwoxford.config.database;

import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * Created by zhuyu on 2017-08-28.
 */
public class BBISOracle12cDialect extends Oracle12cDialect {
    public BBISOracle12cDialect() {
        super();
        registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NVARCHAR,StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.LONGNVARCHAR,StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.DECIMAL,StandardBasicTypes.DOUBLE.getName());
        registerHibernateType(Types.NCLOB,StandardBasicTypes.STRING.getName());
    }
}
