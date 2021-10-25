package daedan.mes.equip.repository;

import daedan.mes.make.domain.OperMast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperMastRepository extends JpaRepository<OperMast, Long> {
    OperMast findByCustNoAndSpotEquipNoAndFrUnixHmsAndUsedYn(Long custNo, Long spotEquipNo, Long frUnixHms,String yn);
    OperMast findByCustNoAndSpotEquipNoAndUsedYn(Long custNo, Long spotEquipNo,String yn);
    OperMast findByCustNoAndOperNoAndSpotEquipNoAndUsedYn(Long custNo, Long operNo, Long spotEquipNo,String yn);
    OperMast findByCustNoAndOperNoAndUsedYn(Long custNo, Long operNo, String yn);
}
