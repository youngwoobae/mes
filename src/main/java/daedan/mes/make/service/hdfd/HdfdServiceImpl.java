package daedan.mes.make.service.hdfd;

import daedan.mes.make.mapper.hdfd.HdfdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("hdfdService")
public class HdfdServiceImpl implements HdfdService {

    @Autowired
    private HdfdMapper mapper;

    @Override
    public List<Map<String, Object>> getHdfdIndcList(Map<String, Object> paraMap){
        return mapper.getHdfdIndcList(paraMap);
    }
}
