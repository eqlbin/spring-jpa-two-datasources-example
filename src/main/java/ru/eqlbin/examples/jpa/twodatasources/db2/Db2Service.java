package ru.eqlbin.examples.jpa.twodatasources.db2;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eqlbin.examples.jpa.twodatasources.dto.Dto;

@Service
@AllArgsConstructor
public class Db2Service {

    private Db2Repository db2Repository;

    @Transactional("db2TransactionManager")
    public Dto createData(Dto dto) {
        Db2Entity entity = db2Repository.save(new Db2Entity(dto.getValue()));
        return new Dto(entity.getId(), entity.getValue());
    }

    @Transactional("db2TransactionManager")
    public Dto getData(Long id) {
        return db2Repository
                .findById(id)
                .map(entity -> new Dto(entity.getId(), entity.getValue())).orElse(null);
    }
}
