package app.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static app.testdata.TestData.initializeTestData;

@SpringJUnitConfig(app.config.DbConfig.class)
@Sql(scripts = "/mysql_test_data_script.sql")
public abstract class AbstractServiceTest {

    @BeforeEach
    public void refreshTestData() {
        initializeTestData();
    }

}
