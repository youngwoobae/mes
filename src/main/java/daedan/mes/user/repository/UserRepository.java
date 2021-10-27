package daedan.mes.user.repository;


import daedan.mes.user.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByMailAddrAndUsedYn(String email,String yn);
    UserInfo findByUserIdAndUsedYn(Long userId,String yn);

    UserInfo findByUserNmAndUsedYn( String userNm, String y);
    UserInfo findByDeptNoAndUserNmAndUsedYn(Long deptNo, String userNm, String y);
    UserInfo findByMakeSeq( Integer makeIdx);
    UserInfo findByToken(String token);
}
