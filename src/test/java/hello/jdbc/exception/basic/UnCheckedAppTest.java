package hello.jdbc.exception.basic;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedAppTest {

    @Test
    public void unchecked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(RuntimeSQLException.class);
    }

    @Test
    public void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        }catch (Exception e){
            //e.printStackTrace();
            log.info("ex", e);
        }
    }

    static class Controller {

        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {

        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {

        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }

    }

    static class Repository {

        public void call() {
            try {
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e); //런타임 예외로 변환해서 던진다.
                //throw new RuntimeSQLException();
                //원인을 넘겨주지 않는다면 어떤 이유로 예외가 발생한줄 알 수가 없다
                //EX) SQLException 이 발생해도, 어떤 SQL이 잘못 실행됫는지 알 수가 없다.
                //예외를 전환 할 떄는 꼭 예외를 전달해 주어야한다.
            }
        }
        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{

        public RuntimeSQLException() {
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
