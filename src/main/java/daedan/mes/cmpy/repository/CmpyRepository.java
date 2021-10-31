package daedan.mes.cmpy.repository;

import daedan.mes.cmpy.domain.CmpyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmpyRepository extends JpaRepository<CmpyInfo, Long> {
    CmpyInfo findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(Long custNo, Long mngrGbnCd, String cmpyNm,String yn);
    CmpyInfo findByCustNoAndMngrGbnCdAndCmpyNoAndUsedYn(Long custNo, Long mngrGbnCd, Long cmpyNo, String y);
    CmpyInfo findByCustNoAndMngrGbnCdAndCmpyNmAndReprMailAddrAndUsedYn(Long custNo, Long mngrGbnCd, String cmNm, String cmMi, String y);
    CmpyInfo findByCustNoAndCmpyNoAndUsedYn(Long custNo, Long cmpyNo, String y);
}
