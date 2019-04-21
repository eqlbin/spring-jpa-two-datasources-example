package ru.eqlbin.examples.jpa.twodatasources.db1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Db1Repository extends JpaRepository<Db1Entity, Long> {
}
