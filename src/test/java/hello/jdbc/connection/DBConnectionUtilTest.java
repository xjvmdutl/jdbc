package hello.jdbc.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class DBConnectionUtilTest {

    @Test
    public void connection(){
        Connection connection = DBConnectionUtil.getConnection(); //구현체로 DB의 구현체를 반환한다.
        assertThat(connection).isNotNull();
    }
}
