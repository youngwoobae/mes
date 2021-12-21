package daedan.mes.sysmenu.user.repository;

import daedan.mes.sysmenu.user.domain.AccHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccHstrRepository extends JpaRepository<AccHstr, Long> {
    AccHstr findByCustNoAndAccNo(Long custNo, Long accNo);
}
