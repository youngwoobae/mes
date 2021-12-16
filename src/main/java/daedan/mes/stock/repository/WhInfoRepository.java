package daedan.mes.stock.repository;

import daedan.mes.stock.domain.WhInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhInfoRepository extends JpaRepository<WhInfo, Long> {
    WhInfo findByCustNoAndWhNoAndUsedYn(Long custNo, Long whNo,String yn);
    WhInfo findByCustNoAndSaveTmprAndWhTpAndWhLocNoAndUsedYn(Long custNo, Long saveTmpr, Long whTp, Byte whLocNo, String y);

    List<WhInfo> findAllByCustNoAndWhNmAndUsedYn(Long custNo, String owhWhNm, String y);

    List<WhInfo> findAllByCustNoAndSaveTmprAndWhTpAndUsedYn(Long custNo, Long saveTmpr, Long whTpSale, String y);
}
