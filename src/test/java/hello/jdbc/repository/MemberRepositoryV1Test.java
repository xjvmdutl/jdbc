package hello.jdbc.repository;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    public void beforeEach(){
        //기본 DriverManager - 항상 새로운 커낵션을 획득
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource); //커낵션 풀링 동작
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV0", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        log.info("findMember == member {} ", findMember == member);
        log.info("findMember == member {} ", findMember.equals(member));
        assertThat(findMember).isEqualTo(member);

        //update : money : 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        //좋은 테스트 방식이 아니다
        // 테스트 데이터를 지우기 전에 오류가 발생하게 되면 데이터가 삭제되지 않기 떄문에 데이터가 남게 된다.

        //delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(
            //조회 할 수 없고, 오류가 발생한다.
            //반복 테스트가 가능해 졋다
            NoSuchElementException.class);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}