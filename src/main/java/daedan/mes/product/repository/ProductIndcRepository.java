package daedan.mes.product.repository;

import daedan.mes.product.domain.ProductIndc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductIndcRepository  extends JpaRepository<ProductIndc, Long> {
    ProductIndc findByCustNoAndMakeFrUtAndProdNoAndUsedYn(Long custNo, Long makeFrUt, Long prodNo, String y);
}
