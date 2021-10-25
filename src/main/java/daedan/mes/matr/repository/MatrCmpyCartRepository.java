package daedan.mes.matr.repository;

import daedan.mes.matr.domain.MatrCmpy;
import daedan.mes.matr.domain.MatrCmpyCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrCmpyCartRepository extends JpaRepository<MatrCmpyCart, Long> {
    void deleteByUserId(Long userId);

}
