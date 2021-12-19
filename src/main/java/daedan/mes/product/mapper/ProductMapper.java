package daedan.mes.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ProductMapper {
    List<Map<String, Object>> getProductPlanList(Map<String, Object> paraMap);
    List<Map<String, Object>> getProductIndcList(Map<String, Object> paraMap);
}
