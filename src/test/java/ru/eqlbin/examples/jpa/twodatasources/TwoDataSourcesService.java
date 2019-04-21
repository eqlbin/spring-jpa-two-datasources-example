package ru.eqlbin.examples.jpa.twodatasources;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import ru.eqlbin.examples.jpa.twodatasources.db1.Db1Entity;
import ru.eqlbin.examples.jpa.twodatasources.db1.Db1Repository;
import ru.eqlbin.examples.jpa.twodatasources.db2.Db2Entity;
import ru.eqlbin.examples.jpa.twodatasources.db2.Db2Repository;
import ru.eqlbin.examples.jpa.twodatasources.dto.Dto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TwoDataSourcesService {

    private Db1Repository db1Repository;
    private Db2Repository db2Repository;

    /**
     * Saves data in persistence context for both data sources and then throws a {@link RuntimeException}.
     * The method uses a simple {@link JpaTransactionManager} configured for the first data source
     * for transaction management. So work with the second data source performs without a transaction.
     *
     * Note that you can not specify a transactionManager as {@link Transactional} annotation parameter,
     * but then the {@link Primary primary} transactionManager bean will be used implicitly.
     *
     * @param bothDbValue value to write to both data sources.
     */
    @Transactional("db1TransactionManager")
    public void createDataWithDb1TransactionManagerAndThrowException(String bothDbValue) {
        db1Repository.save(new Db1Entity(bothDbValue));
        db2Repository.save(new Db2Entity(bothDbValue));

        throw new RuntimeException();
    }

    /**
     * Saves data in persistence context for both data sources and then throws the {@link RuntimeException}.
     * The method uses the {@link ChainedTransactionManager} configured for both data source.
     * This implementation delegates the creation, commits, and rollbacks of transactions to
     * several instances of the {@link PlatformTransactionManager}.
     *
     * @param bothDbValue value to write to both data sources.
     */
    @Transactional("chainedTransactionManager")
    public void createDataWithChainedTransactionManagerAndThrowException(String bothDbValue) {
        db1Repository.save(new Db1Entity(bothDbValue));
        db2Repository.save(new Db2Entity(bothDbValue));

        throw new RuntimeException();
    }

    /**
     * Saves data in persistence context for both data sources.
     * The method uses the {@link ChainedTransactionManager} configured for both data source.
     * This implementation delegates the creation, commits, and rollbacks of transactions to
     * several instances of the {@link PlatformTransactionManager}.
     *
     * Note that this method correctly saves data in the persistence context without
     * throwing the {@link RuntimeException}.
     *
     * @param db1Value value to write to first data sources.
     * @param db2Value value to write to second data sources.
     */
    @Transactional("chainedTransactionManager")
    public void createDataWithChainedTransactionManager(String db1Value, String db2Value) {
        db1Repository.save(new Db1Entity(db1Value));
        db2Repository.save(new Db2Entity(db2Value));
    }

    /**
     * Obtains all data from the first data source.
     */
    @Transactional("db1TransactionManager")
    public List<Dto> getAllFromDb1() {
        return db1Repository
                .findAll().stream()
                .map(entity -> new Dto(entity.getId(), entity.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Obtains all data from the second data source.
     */
    @Transactional("db2TransactionManager")
    public List<Dto> getAllFromDb2() {
        return db2Repository
                .findAll().stream()
                .map(entity -> new Dto(entity.getId(), entity.getValue()))
                .collect(Collectors.toList());
    }
}
