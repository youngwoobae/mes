package daedan.mes.make.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ExcelMakeMapper {
    List<Map<String, Object>> getComboProdProcList(Map<String, Object> paraMap);

    Long getMakeIndcStatus(Map<String, Object> procMap);


}
