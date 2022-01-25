package com.sbm4j.hearthstone.myhearthstone.services.db;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class SqlFunctionsMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction(
                "group_concat",
                new StandardSQLFunction("group_concat", StandardBasicTypes.STRING)
        );

        metadataBuilder.applySqlFunction(
                "greatest", new StandardSQLFunction( "GREATEST", StandardBasicTypes.INTEGER )
        );

        metadataBuilder.applySqlFunction(
                "least", new StandardSQLFunction( "LEAST", StandardBasicTypes.INTEGER )
        );
    }
}
