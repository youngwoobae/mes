package daedan.mes.equip.service;


import daedan.mes.equip.domain.*;
import daedan.mes.equip.mapper.EquipBomMapper;
import daedan.mes.equip.repository.EquipBomCartRepository;
import daedan.mes.equip.repository.EquipBomRepository;
import daedan.mes.equip.repository.EquipInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("equipBomService")
public class EquipBomServiceImpl implements EquipBomService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private EquipBomRepository bomRepository;

    @Autowired
    private EquipBomCartRepository bomCartRepository;


    @Autowired
    private EquipBomMapper bomMapper;

    @Override
    public List<Map<String, Object>> getEquipBomList(Map<String, Object> paraMap) {
        return bomMapper.getEquipBomList(paraMap);
    }

    @Override
    public int getEquipBomListCount(Map<String, Object> paraMap) {
        return bomMapper.getEquipBomListCount(paraMap);
    }

    @Override
    public void saveEquipBomCart(Map<String, Object> map) {

    }

    @Override
    @Transactional
    public void saveEquipBom(Map<String, Object> map) {
        String tag = "MatrService.saveEquipBom => ";
        Long custNo = Long.parseLong(map.get("custNo").toString());
        EquipBom em = new EquipBom();
        Long equipNo = Long.parseLong(map.get("equip_no").toString());
        em.setEquipBomNo(Long.parseLong(map.get("equip_bom_no").toString()));
        em.setEquipNo(equipNo);
        em.setNeedQty(Long.parseLong(map.get("need_qty").toString()));
        em.setModDt(em.getModDt());
        em.setModId(Long.parseLong(map.get("userId").toString()));
        em.setModIp(map.get("ipaddr").toString());
        em.setUsedYn("Y");
        Long matrNo = Long.parseLong(map.get("matr_no").toString());
        em.setMatrNo(matrNo);

        EquipBom eb = bomRepository.findByCustNoAndEquipNoAndMatrNo(custNo,equipNo, matrNo);
        if (eb == null) {
            log.info(tag + "EquipBom For Append = " + em.toString());
        } else {
            em.setCustNo(custNo);
            bomRepository.save(em);
        }
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void dropEquipBom(Map<String, Object> map) {
        Long userId = Long.parseLong(map.get("userId").toString());
        Long custNo = Long.parseLong(map.get("custNo").toString());
        Map<String, Object> bomMap = (Map<String, Object>) map.get("bomInfo");

        Long equipNo = Long.parseLong(bomMap.get("equip_no").toString());
        Long matrNo = Long.parseLong(bomMap.get("matr_no").toString());
        EquipBom eb = bomRepository.findByCustNoAndEquipNoAndMatrNo(custNo,equipNo, matrNo);
        if (eb != null) {
            bomRepository.deleteByCustNoAndEquipNoAndMatrNo(custNo,equipNo, matrNo);
        } else {
            bomCartRepository.deleteByCustNoAndEquipNoAndMatrNoAndUserId(custNo,equipNo, matrNo, userId);
        }
    }

    @Override
    public Map<String, Object> getEquipBomInfo(Map<String, Object> paraMap) {
        Map<String, Object> rsltMap = new HashMap<String, Object>();
        try {
            rsltMap = bomMapper.getEquipBomInfo(paraMap);
        } catch (NullPointerException ne) {
            rsltMap = bomMapper.getEquipBomCartInfo(paraMap);
        }
        return rsltMap;
    }

    @Override
    @Transactional
    public void apndEquipBomCart(Map<String, Object> emMap) {
        String tag = "MatrService.apndEquipBomCart => ";
        Long equipNo = Long.parseLong(emMap.get("equipNo").toString());
        Long custNo = Long.parseLong(emMap.get("custNo").toString());
        List<Map<String, Object>> matrList = (List<Map<String, Object>>) emMap.get("matrList");
        Long userId = Long.parseLong(emMap.get("userId").toString());
        for (Map<String, Object> el : matrList) {
            EquipBomCart bomCart = new EquipBomCart();
            bomCart.setUserId(userId);
            bomCart.setEquipNo(equipNo);
            Long matrNo = Long.parseLong(el.get("matr_no").toString());
            bomCart.setMatrNo(matrNo);

            EquipBomCart cart = bomCartRepository.findByCustNoAndEquipNoAndMatrNo(custNo,equipNo, matrNo);
            if (cart == null) {
                bomCartRepository.save(bomCart);
            }
        }
    }

    @Transactional
    @Override
    public void addEquipBomList(Map<String, Object> paraMap) {
        log.info(paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        EquipBom esvo = new EquipBom();
        esvo.setEquipNo(Long.parseLong(paraMap.get("equipNo").toString()));

        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("equipBomList");
        for (Map<String, Object> el : ds) {
            esvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            esvo.setModDt(esvo.getModDt());
            esvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            esvo.setModIp(paraMap.get("ipaddr").toString());
            esvo.setUsedYn("Y");

            EquipBom chkvo = bomRepository.findByCustNoAndEquipNoAndMatrNo(custNo,esvo.getEquipNo(), esvo.getMatrNo());
            if (chkvo != null) {
                esvo.setEquipBomNo(chkvo.getEquipBomNo());
            }
            else {
                esvo.setEquipBomNo(0L);
                esvo.setRegDt(esvo.getRegDt());
                esvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                esvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            esvo.setCustNo(custNo);
            bomRepository.save(esvo);

        }
    }

}