package daedan.mes.imp.service.mihong;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MhImpService {
    @Transactional
    void ordInfoByExcel(Map<String, Object> paraMap) throws Exception ;



}
