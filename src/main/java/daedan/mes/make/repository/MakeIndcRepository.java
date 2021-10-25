package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeIndc;
import daedan.mes.prod.domain.ProdInfo;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MakeIndcRepository extends JpaRepository<MakeIndc, Long> {
    MakeIndc findByCustNoAndIndcNoAndProcCdAndUsedYn(Long custNo, Long indcNo, Long procCd, String y);
    MakeIndc findByCustNoAndParIndcNoAndIndcNoAndProcCdAndUsedYn(Long custNo, Long parIndcNo, Long indcNo, Long procCd, String y);
    MakeIndc findByCustNoAndIndcQtyAndIndcNoAndProcCdAndUsedYn(Long custNo, Float indcQty, Long indcNo, Long procCd, String y);
    MakeIndc findByCustNoAndIndcNoAndUsedYn(Long custNo, Long indcNo, String y);


    MakeIndc findByCustNoAndParIndcNoAndProcCdAndUsedYn(Long custNo, Long indcNo, Long procCd, String usedYn);

    MakeIndc findByCustNoAndProdNoAndIndcWgtAndIndcDtAndUsedYn(Long custNo, Long prodNo, Float indcWgt, Date indcDt, String y);

    MakeIndc findByCustNoAndIndcNoAndIndcStsAndUsedYn(Long custNo, Long parIndcNo, Long indcSts, String y);
}
