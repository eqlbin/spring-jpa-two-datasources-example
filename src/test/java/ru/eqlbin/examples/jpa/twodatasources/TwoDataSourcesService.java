package ru.eqlbin.examples.jpa.twodatasources;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Transactional("db1TransactionManager")
    public void createDataWithDb1TransactionManagerAndThrowException(String bothDbValue) {
        db1Repository.save(new Db1Entity(bothDbValue));
        db2Repository.save(new Db2Entity(bothDbValue));

        throw new RuntimeException();
    }

    @Transactional("chainedTransactionManager")
    public void createDataWithChainedTransactionManagerAndThrowException(String bothDbValue) {
        db1Repository.save(new Db1Entity(bothDbValue));
        db2Repository.save(new Db2Entity(bothDbValue));

        throw new RuntimeException();
    }

    @Transactional("chainedTransactionManager")
    public void createDataWithChainedTransactionManager(String db1Value, String db2Value) {
        db1Repository.save(new Db1Entity(db1Value));
        db2Repository.save(new Db2Entity(db2Value));
    }

    @Transactional("db1TransactionManager")
    public List<Dto> getAllFromDb1() {
        return db1Repository
                .findAll().stream()
                .map(entity -> new Dto(entity.getId(), entity.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional("db2TransactionManager")
    public List<Dto> getAllFromDb2() {
        return db2Repository
                .findAll().stream()
                .map(entity -> new Dto(entity.getId(), entity.getValue()))
                .collect(Collectors.toList());
    }

}
