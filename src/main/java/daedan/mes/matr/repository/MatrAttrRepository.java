package daedan.mes.matr.repository;

import daedan.mes.matr.domain.MatrAttr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrAttrRepository  extends JpaRepository<MatrAttr, Long> {

    MatrAttr findByCustNoAndMatrNoAndUsedYn(Long custNo , long matr_no, String y);
}
