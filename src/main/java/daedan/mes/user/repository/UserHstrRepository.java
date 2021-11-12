package daedan.mes.user.repository;


import daedan.mes.user.domain.UserHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHstrRepository extends JpaRepository<UserHstr, Long> {
    UserHstr findByCustNoAndUserIdAndHstrDtAndUsedYn(Long custNo, Object userNo, String hstrDt, String y);
}
