package daedan.mes.user.repository;

import daedan.mes.user.domain.UserWork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWorkRepository  extends JpaRepository<UserWork, Long> {
    UserWork findByCustNoAndWorkNoAndUsedYn(Long custNo, Long workNo, String y);
    UserWork findByCustNoAndWorkDtAndUserIdAndUsedYn(Long custNo, String workDt, Long workerId, String y);
}
