package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakePlanRepository  extends JpaRepository<MakePlan, Long> {
    MakePlan findByCustNoAndPlanUtAndProdNoAndUsedYn(Long custNo, Long planUt, Long prodNo, String y);
}
