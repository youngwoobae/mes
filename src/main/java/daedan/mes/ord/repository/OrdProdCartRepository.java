package daedan.mes.ord.repository;

import daedan.mes.ord.domain.OrdProdCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdProdCartRepository extends JpaRepository<OrdProdCart, Long> {
    OrdProdCart findByCustNoAndUserIdAndProdNo(Long custNo,Long userId, Long prodNo);
    OrdProdCart  findByCustNoAndUserIdAndOrdNoAndProdNo(Long custNo,Long userId, Long ordNo, Long prodNo);
}
