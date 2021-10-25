package daedan.mes.modbus.repository;

import daedan.mes.modbus.domain.TmprHstr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmprHstrRepository extends JpaRepository<TmprHstr, Long> {

    TmprHstr findByCustNoAndUnixHms(Long custNo,Long unixHms);
}
