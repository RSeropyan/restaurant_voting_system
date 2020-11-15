package app.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(classes = {app.config.WebConfig.class, app.config.DbConfig.class, app.config.CacheConfig.class})
@Sql(scripts = "/db_population.sql")
@Transactional
abstract public class ServiceTest {


}
