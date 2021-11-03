package daedan.mes.make.repository;

import daedan.mes.make.domain.youjin.MetalLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RcvMetalRepository extends JpaRepository<MetalLog, Long> {
}
