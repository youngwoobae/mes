package daedan.mes.common.service.util;
import org.springframework.jdbc.support.JdbcUtils;

import java.util.HashMap;

public class CamelListMap extends HashMap {
    @Override
    public Object put(Object key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String)key), value);
    }
}

