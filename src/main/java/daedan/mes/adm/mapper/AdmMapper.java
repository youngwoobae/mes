package daedan.mes.adm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AdmMapper {
    List<Map<String, Object>> getSysMenuList(Map<String, Object> cond);
}
