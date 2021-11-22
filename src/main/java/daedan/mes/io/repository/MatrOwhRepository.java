package daedan.mes.io.repository;

import daedan.mes.io.domain.MatrOwh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatrOwhRepository extends JpaRepository<MatrOwh, Long> {
    MatrOwh findByCustNoAndIndcNoAndMatrNoAndUsedYn(Long custNo, Long indcNo, Long matrNo, String yn);
    MatrOwh findByCustNoAndIndcNoAndMatrNoAndWhNoAndUsedYn(Long custNo, Long indcNo, Long matrNo, Long whNo, String Yn);
    MatrOwh findByCustNoAndOwhNoAndUsedYn(Long custNo, Long owhNo, String y);

    MatrOwh findByCustNoAndIndcNoAndUsedYn(Long custNo, Long indcNo, String y);
    List<MatrOwh> findAllByCustNoAndIndcNoAndUsedYn(Long custNo, Long indcNo, String y);
}
