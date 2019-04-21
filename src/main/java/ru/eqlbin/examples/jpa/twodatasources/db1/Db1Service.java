package ru.eqlbin.examples.jpa.twodatasources.db1;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eqlbin.examples.jpa.twodatasources.dto.Dto;

@Service
@AllArgsConstructor
public class Db1Service {

    private Db1Repository db1Repository;

    @Transactional("db1TransactionManager")
    public Dto createData(Dto dto) {
        Db1Entity entity = db1Repository.save(new Db1Entity(dto.getValue()));
        return new Dto(entity.getId(), entity.getValue());
    }

    @Transactional("db1TransactionManager")
    public Dto getData(Long id) {
        return db1Repository
                .findById(id)
                .map(entity -> new Dto(entity.getId(), entity.getValue())).orElse(null);
    }
}
