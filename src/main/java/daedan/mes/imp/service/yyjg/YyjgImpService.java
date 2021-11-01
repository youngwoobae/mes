package daedan.mes.imp.service.yyjg;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface YyjgImpService {
    @Transactional
    ArrayList<Map<String, Object>> makeStkByExcel(Map<String, Object> paraMap);
}
