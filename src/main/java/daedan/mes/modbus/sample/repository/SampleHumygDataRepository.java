package daedan.mes.modbus.sample.repository;

import daedan.mes.modbus.sample.domain.SampleHumygData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleHumygDataRepository extends JpaRepository<SampleHumygData, Long> {
    SampleHumygData findByKeyNo(Long keyNo);
}

