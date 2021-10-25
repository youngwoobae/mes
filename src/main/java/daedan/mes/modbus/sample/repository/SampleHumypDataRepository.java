package daedan.mes.modbus.sample.repository;

import daedan.mes.modbus.sample.domain.SampleHumypData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleHumypDataRepository extends JpaRepository<SampleHumypData, Long> {
    SampleHumypData findByKeyNo(Long keyNo);
}

