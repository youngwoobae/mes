package daedan.mes.make.service.metal10;

import javax.transaction.Transactional;
import java.util.Map;

public interface Metal10Service {
    @Transactional
    void saveMetalLog(Map<String, Object> paraMap);
}
