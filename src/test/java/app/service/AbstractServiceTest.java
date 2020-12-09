package app.service;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;


//@ActiveProfiles("dev")
@SpringJUnitConfig(app.config.DbConfig.class)
@Sql(scripts = "/mysql_script.sql")
//@Sql(scripts = {"/h2_script.sql"})
@Transactional
abstract public class AbstractServiceTest {


}
