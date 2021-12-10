package daedan.mes.prod.repository;

import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.prod.domain.ProdInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdRepository extends JpaRepository<ProdInfo, Long> {
    ProdInfo findByCustNoAndProdNoAndUsedYn(Long custNo, Long prodNo, String y);
    ProdInfo findByCustNoAndProdCodeAndUsedYn(Long custNo, String prodCode, String y);
    ProdInfo findByCustNoAndProdNmAndErpProdNmAndUsedYn(Long custNo, String prodNm, String erpProdNm, String y);
    ProdInfo findByCustNoAndProdNmAndUsedYn(Long custNo, String prodNm, String y);
    ProdInfo findByCustNoAndBrnchNoAndProdNmAndUsedYn(Long custNo, Long brnchNo, String prodNm, String y);
    ProdInfo findByCustNoAndErpProdNmAndUsedYn(Long custNo, String erProdNm, String y);
    ProdInfo findByCustNoAndProdCodeAndProdNmAndUsedYn(Long custNo, String stringCellValue, String stringCellValue1, String y);

}
