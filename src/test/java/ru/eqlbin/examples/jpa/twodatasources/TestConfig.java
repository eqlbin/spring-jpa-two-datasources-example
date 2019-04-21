package ru.eqlbin.examples.jpa.twodatasources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
public class TestConfig {

    @Bean(name = "chainedTransactionManager")
    public PlatformTransactionManager chainedTransactionManager(
            @Qualifier("db1TransactionManager") PlatformTransactionManager db1TransactionManager,
            @Qualifier("db2TransactionManager") PlatformTransactionManager db2TransactionManager) {
        return new ChainedTransactionManager(db1TransactionManager, db2TransactionManager);
    }

}
