package app.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {app.config.AppConfig.class, app.config.DbConfig.class})
@Sql(scripts = "/db_population.sql")
abstract public class ServiceTest {


}
