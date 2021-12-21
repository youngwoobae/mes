package daedan.mes.sysmenu.user.repository;

import daedan.mes.sysmenu.user.domain.CustInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustInfoRepository  extends JpaRepository<CustInfo, Long> {
    CustInfo findByCustNoAndUsedYn(Long custNo,String y);
    CustInfo findByLcnsCdAndUsedYn(String lcnsCd, String y);
    CustInfo findBySaupNoAndUsedYn(String saupNo, String y);
}
