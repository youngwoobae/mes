package daedan.mes.bord.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface BordMapper {
    int getBordListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getBordList(Map<String, Object> paraMap);
    void updateBord(Map<String, Object> paraMap);

    Map<String, Object> getFindBoardTerm(Map<String, Object> paraMap);
}