package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.SQLException;

public interface MemberRepositoryEx { //해당 인터페이스가 checked 예외 때문에 특정 기술에 종속이 되어버린다
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
