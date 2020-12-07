package app.service;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("dev")
@SpringJUnitConfig(app.config.DbConfig.class)
@Sql(scripts = "/db_population.sql")
@Transactional
abstract public class AbstractServiceTest {


}
