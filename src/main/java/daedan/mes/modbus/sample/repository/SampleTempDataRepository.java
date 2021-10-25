package daedan.mes.modbus.sample.repository;

import daedan.mes.modbus.sample.domain.SampleTempData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleTempDataRepository extends JpaRepository<SampleTempData, Long> {
    SampleTempData findByKeyNo(Long keyNo);
}
