package daedan.mes.prod.repository;

import daedan.mes.prod.domain.ProdBrnch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdBrnchRepository  extends JpaRepository<ProdBrnch, Long> {
    ProdBrnch findByCustNoAndBrnchNmAndUsedYn(Long custNo,String brnchNm,String yn);
    ProdBrnch findByCustNoAndParBrnchNoAndBrnchNmAndUsedYn(Long custNo,Long prod_start_brnch, String stringCellValue,String yn);

}
