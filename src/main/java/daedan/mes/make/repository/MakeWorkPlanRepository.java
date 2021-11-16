package daedan.mes.make.repository;

import daedan.mes.make.domain.MakeWorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MakeWorkPlanRepository extends JpaRepository<MakeWorkPlan, Long> {

    MakeWorkPlan findByCustNoAndPlanDtAndBrnchNoAndUsedYn(Long custNo, Date planDt, long brnchNo, String y);
}
