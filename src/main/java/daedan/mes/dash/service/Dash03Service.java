package daedan.mes.dash.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface Dash03Service {
    @Transactional
    List<Map<String, Object>> getTmpr03List(Map<String, Object> paraMap);
}
