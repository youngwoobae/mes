package daedan.mes.dept.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DeptMapper {
    List<Map<String, Object>> getDeptList(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboDeptList(Map<String, Object> paraMap);
    int getDeptListDepth(Map<String, Object> paraMap);
    void appendDeptInfo(Map<String, Object> map);
}
