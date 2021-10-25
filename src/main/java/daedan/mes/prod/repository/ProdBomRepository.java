package daedan.mes.prod.repository;

import daedan.mes.prod.domain.ProdBom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdBomRepository extends JpaRepository<ProdBom, Long> {

    ProdBom findByCustNoAndProdNoAndMatrNoAndUsedYn(Long custNo, Long prodNo, Long matrNo,String yn);
    ProdBom findByCustNoAndBomNoAndUsedYn(Long custNo ,Long bomNo, String y);
    ProdBom findByCustNoAndProdNoAndUsedYn(Long custNo, Long prodNo,String y);

}
