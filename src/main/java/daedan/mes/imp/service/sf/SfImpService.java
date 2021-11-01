package daedan.mes.imp.service.sf;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public interface SfImpService {
    @Transactional
    ArrayList<List<Map<String, Object>>> chkProdExist(HashMap<String, Object> paraMap) throws Exception;
}
