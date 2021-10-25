package daedan.mes.matr.repository;

import daedan.mes.matr.domain.MatrCmpy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrCmpyRepository extends JpaRepository<MatrCmpy, Long> {
    MatrCmpy findByCustNoAndMatrNoAndCmpyNoAndUsedYn(Long custNo, Long matrNo, Long cmpyNo,String yn);

    MatrCmpy findByCustNoAndMatrNoAndUsedYn(Long custNo, Long matrNo, String y);


}
