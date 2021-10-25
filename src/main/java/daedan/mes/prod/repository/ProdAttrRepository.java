package daedan.mes.prod.repository;

import daedan.mes.prod.domain.ProdAttr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdAttrRepository extends JpaRepository<ProdAttr, Long> {
    ProdAttr findByCustNoAndProdNoAndUsedYn(Long custNo,Long prodNo, String y);
}
