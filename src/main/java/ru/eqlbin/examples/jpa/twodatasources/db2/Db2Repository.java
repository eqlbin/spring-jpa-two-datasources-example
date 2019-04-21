package ru.eqlbin.examples.jpa.twodatasources.db2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Db2Repository extends JpaRepository<Db2Entity, Long> {
}
