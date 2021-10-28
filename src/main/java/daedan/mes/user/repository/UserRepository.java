package daedan.mes.user.repository;


import daedan.mes.user.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByMailAddrAndUsedYn(String mailAddr, String y);
    UserInfo findByUserIdAndUsedYn(Long userId,String yn);
    UserInfo findByToken(String token);
    UserInfo findByUserNmAndUsedYn( String userNm, String y);
    UserInfo findByMakeSeq(int userNo);
    UserInfo findByUserId(Long userId);
}
