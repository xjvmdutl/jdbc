package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ConnectionTest {

    @Test
    public void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    @Test
    public void dataSourceDriverManager() throws SQLException {
        //DriverManagerDataSource - 항상 새로운 커낵션을 획득
        //DataSource 를 통해서 가지고 온다는 차이가 있다
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    public void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000); //테스트는 별도의 쓰레드로 동작하기 때문에 Pool에 추가되는 로그를 보기 위해 별도의 Sleep을 주었다
    }
    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection(); //사용하는 시점에는 URL, USERNAME, PASSWORD 정보가 필요가 없다
        Connection con2 = dataSource.getConnection();
        Connection con3 = dataSource.getConnection();
        Connection con4 = dataSource.getConnection();
        Connection con5 = dataSource.getConnection();
        Connection con6 = dataSource.getConnection();
        Connection con7 = dataSource.getConnection();
        Connection con8 = dataSource.getConnection();
        Connection con9 = dataSource.getConnection();
        Connection con10 = dataSource.getConnection();
        Connection con11 = dataSource.getConnection();
        //ConnectionPool이 반환 될떄까지 대기를한다(일정 시간 후 예외 발생)
        //어느정도 기다렸다가 예외를 발생시킬지 설정 가능하다
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }


}
