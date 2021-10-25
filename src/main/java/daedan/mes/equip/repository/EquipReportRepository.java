package daedan.mes.equip.repository;

import daedan.mes.equip.domain.EquipReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipReportRepository extends JpaRepository<EquipReport, Long> {

    EquipReport findByEquipReportNoAndUsedYn(Long equipReportNo, String y);
}
