package daedan.mes.equip.repository;

import daedan.mes.make.domain.OperMast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperMastRepository extends JpaRepository<OperMast, Long> {
    OperMast findByCustNoAndSpotEquipNoAndFrUnixHms(Long custNo, Long spotEquipNo, Long frUnixHms);
    OperMast findByCustNoAndSpotEquipNo(Long custNo, Long spotEquipNo);
    OperMast findByCustNoAndOperNoAndSpotEquipNo(Long custNo, Long operNo, Long spotEquipNo);
    OperMast findByCustNoAndOperNo(Long custNo, Long operNo);
}
