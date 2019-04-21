package ru.eqlbin.examples.jpa.twodatasources.db2;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "entities")
public class Db2Entity {

    /**
     * Идентификатор
     */
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg")
    @SequenceGenerator(name = "sg", sequenceName = "SEQ", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Значение
     */
    @Column(name = "value", nullable = false)
    private String value;

    public Db2Entity(String value) {
        this.value = value;
    }
}
