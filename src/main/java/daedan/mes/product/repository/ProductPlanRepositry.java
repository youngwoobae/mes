package daedan.mes.product.repository;


import daedan.mes.product.domain.ProductIndc;
import daedan.mes.product.domain.ProductPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPlanRepositry  extends JpaRepository<ProductPlan, Long> {
    ProductPlan findByCustNoAndPlanUtAndProdNoAndUsedYn(Long custNo, Long planUt, Long prodNo, String y);
    ProductIndc findByCustNoAndProductPlanNoAndUsedYn(Long custNo, Long prodcustPlanNo, String y);
}
