package ru.eqlbin.examples.jpa.twodatasources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.eqlbin.examples.jpa.twodatasources.db1.Db1Repository;
import ru.eqlbin.examples.jpa.twodatasources.db1.Db1Service;
import ru.eqlbin.examples.jpa.twodatasources.db2.Db2Repository;
import ru.eqlbin.examples.jpa.twodatasources.db2.Db2Service;
import ru.eqlbin.examples.jpa.twodatasources.dto.Dto;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestConfig.class})
public class TwoDataSourcesTest {

    @Autowired
    private Db1Service db1Service;

    @Autowired
    private Db1Repository db1Repository;

    @Autowired
    private Db2Service db2Service;

    @Autowired
    private Db2Repository db2Repository;

    @Autowired
    private TwoDataSourcesService twoDataSourcesService;

    private static final String VALUE = "data";

    @Before
    public void dbCleanup(){
        db1Repository.deleteAll();
        assertThat(db1Repository.findAll(), empty());

        db2Repository.deleteAll();
        assertThat(db2Repository.findAll(), empty());
    }

    @Test
    public void createDataWithDb1TransactionManagerAndThrowException() {
        try {
            twoDataSourcesService.createDataWithDb1TransactionManagerAndThrowException(VALUE);
        } catch (Exception e) {
            // ignore
        }

        // rolled back
        List<Dto> allFromDb1 = twoDataSourcesService.getAllFromDb1();
        assertThat(allFromDb1, empty());

        // committed
        List<Dto> allFromDb2 = twoDataSourcesService.getAllFromDb2();
        assertThat(allFromDb2, not(empty()));
    }

    @Test
    public void createDataWithChainedTransactionManagerAndThrowException() {
        try {
            twoDataSourcesService.createDataWithChainedTransactionManagerAndThrowException(VALUE);
        } catch (Exception e) {
            // ignore
        }

        // rolled back
        List<Dto> allFromDb1 = twoDataSourcesService.getAllFromDb1();
        assertThat(allFromDb1, empty());

        // rolled back
        List<Dto> allFromDb2 = twoDataSourcesService.getAllFromDb2();
        assertThat(allFromDb2, empty());
    }

    @Test
    public void createDataWithChainedTransactionManagerFailOnFirstCommit() {
        try {
            twoDataSourcesService.createDataWithChainedTransactionManager(VALUE, null);
        } catch (Exception e) {
            // ignore
        }

        // rolled back
        List<Dto> allFromDb1 = twoDataSourcesService.getAllFromDb1();
        assertThat(allFromDb1, empty());

        // rolled back
        List<Dto> allFromDb2 = twoDataSourcesService.getAllFromDb2();
        assertThat(allFromDb2, empty());
    }

    @Test
    public void createDataWithChainedTransactionManagerFailOnSecondCommit() {
        try {
            twoDataSourcesService.createDataWithChainedTransactionManager(null, VALUE);
        } catch (Exception e) {
            // ignore
        }

        // rolled back
        List<Dto> allFromDb1 = twoDataSourcesService.getAllFromDb1();
        assertThat(allFromDb1, empty());

        // committed
        List<Dto> allFromDb2 = twoDataSourcesService.getAllFromDb2();
        assertThat(allFromDb2, not(empty()));
    }

    @Test
    public void db1ServiceCreateAndGetData() {
        Dto createdData = db1Service.createData(new Dto(VALUE));
        assertNotNull(createdData);
        assertEquals(VALUE, createdData.getValue());

        Dto gottenData = db1Service.getData(createdData.getId());
        assertEquals(createdData, gottenData);
    }

    @Test
    public void db2ServiceCreateAndGetData() {
        Dto createdData = db2Service.createData(new Dto(VALUE));
        assertNotNull(createdData);
        assertEquals(VALUE, createdData.getValue());

        Dto gottenData = db2Service.getData(createdData.getId());
        assertEquals(createdData, gottenData);
    }
}