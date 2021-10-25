package daedan.mes.user.repository;

import daedan.mes.user.domain.AccHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccHstrRepository extends JpaRepository<AccHstr, Long> {
    AccHstr findByCustNoAndAccNo(Long custNo, Long accNo);
}
