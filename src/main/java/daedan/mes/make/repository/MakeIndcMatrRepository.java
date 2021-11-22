package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeIndcMatr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MakeIndcMatrRepository extends JpaRepository<MakeIndcMatr, Long> {
    MakeIndcMatr findByCustNoAndIndcNoAndMatrNoAndUsedYn(Long custNo, Long indcNo, Long matrNo,String yn);

    MakeIndcMatr findByCustNoAndMatrNoAndUsedYn(Long custNo, Long matrNo, String y);

    MakeIndcMatr findByCustNoAndIndcNoAndUsedYn(Long custNo,Long indcNo, String y);

    List<MakeIndcMatr> findAllByCustNoAndIndcNoAndUsedYn(Long custNo, Long indcNo, String y);
}
