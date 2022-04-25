package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final MemberRepositoryV2 memberRepository;
    private final DataSource dataSource;


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();

        try{
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, money);
            con.commit(); //성공시 커밋
        }catch (Exception e){
            con.rollback();
            throw new IllegalStateException(e);
        }finally {
            release(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money)
        throws SQLException {
        //비지니스 시작
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
        //비지니스 종료
    }

    private void validation(Member toMember) {
        if( toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void release(Connection con) {
        if(con != null){
            try {
                con.setAutoCommit(true); //커넥션 풀에서 해당 커낵션을 사용할 수 있기에 항상  true로 변환
                con.close();
            }catch (Exception e){
                log.error("error", e);
            }
        }
    }
}
