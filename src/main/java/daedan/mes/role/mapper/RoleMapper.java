package daedan.mes.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface RoleMapper {

    List<Map<String, Object>> getRoleMenuList(Map<String, Object> map);
    int getRoleMenuListCount(HashMap<String, Object> paraMap);

    Object getRoleMenuInfo(Long sysMenuNo);

}
