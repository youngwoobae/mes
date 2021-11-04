package daedan.mes.dash.service.mh;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface Dash18Service {
    @Transactional
    List<Map<String, Object>> getMhTmprList(Map<String, Object> paraMap);
}
