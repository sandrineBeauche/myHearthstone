package com.sbm4j.hearthstone.myhearthstone.services.db;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomHSQLDBDialect extends HSQLDialect {

    public CustomHSQLDBDialect(){
        super();

        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
        registerFunction("greatest", new StandardSQLFunction( "GREATEST"));
        registerFunction("least", new StandardSQLFunction( "LEAST"));

        registerFunction("group_concat_distinct",
                new StandardSQLFunction("group_concat_distinct", StandardBasicTypes.STRING));
    }
}
