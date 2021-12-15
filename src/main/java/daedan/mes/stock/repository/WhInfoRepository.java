package daedan.mes.stock.repository;

import daedan.mes.stock.domain.WhInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhInfoRepository extends JpaRepository<WhInfo, Long> {
    WhInfo findByCustNoAndWhTpAndSaveTmprAndWhNmAndUsedYn(Long custNo, Long whTp, Long saveTmpr, String whNm,String yn);
    WhInfo findByCustNoAndWhNoAndUsedYn(Long custNo, Long whNo,String yn);
    WhInfo findByCustNoAndWhNmAndUsedYn(Long custNo, String whNm, String y);
    WhInfo findByCustNoAndSaveTmprAndWhTpAndUsedYn(Long custNok, Long saveTmpr, Long whTp, String y);
    List<WhInfo> findAllByCustNoAndSaveTmprAndWhTpAndUsedYn(Long custNo, Long saveTmpr, Long whTpSale, String y);

    WhInfo findByCustNoAndSaveTmprAndWhTpAndUsedYnAndWhLocSeq(Long custNo, long saveTmpr, long wh_tp, String y, long wh_loc_seq);

//    void save();


}
