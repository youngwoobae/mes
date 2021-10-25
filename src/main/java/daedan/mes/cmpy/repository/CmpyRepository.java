package daedan.mes.cmpy.repository;

import daedan.mes.cmpy.domain.CmpyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmpyRepository extends JpaRepository<CmpyInfo, Long> {
    CmpyInfo findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(Long custNo, Long cmpyTp, String cmpyNm,String yn);
    CmpyInfo findByCustNoAndCmpyTpAndCmpyNoAndUsedYn(Long custNo, Long cmpyTp, Long cmpyNo, String y);
    CmpyInfo findByCustNoAndCmpyTpAndCmpyNmAndReprMailAddrAndUsedYn(Long custNo, Long cmpyTp, String cmNm, String cmMi, String y);
    CmpyInfo findByCustNoAndCmpyNoAndUsedYn(Long custNo, Long cmpyNo, String y);
}
