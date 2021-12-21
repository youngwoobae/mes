package daedan.mes.sysmenu.user.repository;

import daedan.mes.sysmenu.user.domain.UserWork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWorkRepository  extends JpaRepository<UserWork, Long> {
    UserWork findByCustNoAndWorkNoAndUsedYn(Long custNo, Long workNo, String y);
}
