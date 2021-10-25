package daedan.mes.proc.repository;

import daedan.mes.proc.domain.ProcMp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcMpRepository extends JpaRepository<ProcMp, Long> {
    ProcMp findByProcGrpCdAndUserId(Long procGrpCd, long user_id);
}
