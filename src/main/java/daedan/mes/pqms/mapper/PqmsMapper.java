package daedan.mes.pqms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PqmsMapper {
    List<Map<String, Object>> getOrdRecvList(Map<String, Object> paraMap);

    int getOrdRecvListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboOrdSender(Map<String, Object> paraMap);
}
