package daedan.mes.file.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface FileMapper {
    void revivalFileUsed(Map<String, Object> map);
    Map<String, Object> getImageUrl(Map<String, Object> paraMap);
}
