package daedan.mes.user.repository;

import daedan.mes.user.domain.CustInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface CustInfoRepository  extends JpaRepository<CustInfo, Long> {
    CustInfo findByCustNo(Long custNo);
    CustInfo findByLcnsCd(String lcnsCd);
}
