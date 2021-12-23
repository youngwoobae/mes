package daedan.mes.io.repository;

import daedan.mes.io.domain.ProdOwh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ProdOwhRepository  extends JpaRepository<ProdOwh, Long> {
    ProdOwh findByCustNoAndOrdNoAndProdNoAndUsedYn(Long custNo, Long ordNo, Long prodNo, String y);
    ProdOwh findByCustNoAndCmpyNoAndProdNoAndWhNoAndOrdNoAndUsedYn(Long custNo, Long cmpyNo, Long prodNo, Long whNo, Long ordNo, String usedYn);
    ProdOwh findByCustNoAndOwhNoAndUsedYn(Long custNo, Long owhNo, String y);
    ProdOwh findByCustNoAndCmpyNoAndProdNoAndAndSendUtAndUsedYn(Long custNo, Long cmpyNo, Long prodNo, Long sendUt, String y);
}
