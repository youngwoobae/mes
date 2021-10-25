package daedan.mes.pms.repository;

import daedan.mes.pms.domain.PmsWho;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PmsWhoRepository   extends JpaRepository<PmsWho, Long> {
    PmsWho findByPmsWhoNoAndUsedYn(Long pmsWhoNo, String usedYn);
}
