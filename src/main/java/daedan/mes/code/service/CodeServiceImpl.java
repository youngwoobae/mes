package daedan.mes.code.service;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.mapper.CodeMapper;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service("CodeService")
public class CodeServiceImpl implements CodeService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private CodeMapper mapper;

    private List<Map<String,Object>> elist = null;
    LinkedHashMap <String,Object> root  = new LinkedHashMap < String, Object > ();

    @Override
    public List<Map<String, Object>> getCodeList(Map<String, Object> paraMap) {
        return mapper.getCodeList(paraMap);
    }

    @Override
    public int getCodeListCount(Map<String, Object> paraMap) {
        return mapper.getCodeListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboCodeList(Map<String, Object> paraMap) {
        String tag = "CodeService.getComboCodeList => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        return mapper.getComboCodeList(paraMap);
    }
    @Override
    public List<Map<String, Object>> getComboUserDeptList(Map<String, Object> paraMap) {
        String tag = "CodeService.getComboUserDeptList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboUserDeptList(paraMap);
    }
    @Override
    public List<Map<String, Object>> getComboWithoutChoiceCodeList(Map<String, Object> paraMap) {
        String tag = "CodeService.getComboWithoutChoiceCodeList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboWithoutChoiceCodeList(paraMap);
    }

    @Override
    public CodeInfo findByParCodeNoAndCodeNmAndUsedYn(long l, String fileExtNm,String yn) {
        return codeRepo.findByParCodeNoAndCodeNmAndUsedYn(l,fileExtNm,yn);
    }
    @Transactional
    @Override
    public void codeModify(Map<String, Object> paraMap) {
        CodeInfo codeIn = new CodeInfo();
        Map<String, Object> passMap = (Map<String, Object>) paraMap.get("codeInfo");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        codeIn.setCodeNm((String) paraMap.get("code_nm"));
        codeIn.setCodeAlais((String) paraMap.get("code_alais"));
        codeIn.setCodeBrief((String) paraMap.get("code_brief"));
        codeIn.setCodeNo(Long.parseLong(paraMap.get("code_no").toString()));

        codeIn.setCodeSeq((int) Long.parseLong (paraMap.get("code_seq").toString()));
        codeIn.setParCodeNo(Long.parseLong(paraMap.get("par_code_no").toString()));
        codeIn.setModableYn((String) paraMap.get("mod_able_yn"));
        codeIn.setUsedYn("Y");

// 수정일자 등록
        codeIn.setModDt(codeIn.getModDt());
        codeIn.setModIp(paraMap.get("ipaddr").toString());
        codeIn.setCustNo(custNo);
        codeRepo.save(codeIn);
        mapper.Modify(paraMap);
    }

    @Override
    public Map<String, Object> getCodeInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.CodeService.getCodeInfo";
        log.info(tag + "paraMap = " +paraMap.toString());
        CodeInfo cvo = new CodeInfo();
        cvo.setCodeNo(Long.parseLong(paraMap.get("codeNo").toString()));
        CodeInfo rvo = codeRepo.findByCodeNmAndUsedYn(cvo.getCodeNm(),"Y");
        return StringUtil.voToMap(rvo);
    }

    @Transactional
    @Override
    public Long saveInspCode(Map<String, Object> paraMap) {
        String tag = "vsvc.CodeService.saveInspCode";
        log.info(tag + "paraMap = " +paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        CodeInfo vo = new CodeInfo();
        vo.setParCodeNo(Long.parseLong(paraMap.get("par_code_no").toString()));
        vo.setCodeNm(paraMap.get("code_nm").toString());
        vo.setCodeSeq((int) Long.parseLong (paraMap.get("code_seq").toString()));
        vo.setModDt(DateUtils.getCurrentDateTime());
        vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        vo.setModIp(paraMap.get("ipaddr").toString());
        vo.setModableYn("Y");
        vo.setUsedYn("Y");
        try {
            vo.setCodeAlais(paraMap.get("code_alais").toString());
        }
        catch (NullPointerException ne) {
            vo.setCodeAlais(vo.getCodeNm());
        }
        try {
            vo.setCodeAlais(paraMap.get("code_brief").toString());
        }
        catch (NullPointerException ne) {
            vo.setCodeBrief(vo.getCodeNm());
        }
        try {
            vo.setCodeNo(Long.parseLong(paraMap.get("code_no").toString()));
        }
        catch (NullPointerException ne) {

        }
        CodeInfo chkvo = codeRepo.findByCodeNoAndUsedYn(vo.getCodeNo(),"Y");
        if (chkvo != null) {
            vo.setCodeNo(chkvo.getCodeNo());
        }
        else {
            vo.setRegDt(DateUtils.getCurrentDateTime());
            vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setRegIp(paraMap.get("ipaddr").toString());
        }
        vo.setCustNo(custNo);
        vo = codeRepo.save(vo);
        return vo.getCodeNo();
    }

    @Override
    public void dropCode(Map<String, Object> paraMap) {
        Long codeNo = Long.parseLong(paraMap.get("codeNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        CodeInfo vo = codeRepo.findByCodeNoAndUsedYn(codeNo,"Y");
        if (vo != null) {
            vo.setUsedYn("N");
            codeRepo.save(vo);
        }
    }
    @Override
    public CodeInfo saveCodeByName(Map<String, Object> paraMap) {
        CodeInfo vo = new CodeInfo();
        vo.setUsedYn("Y");
        try {
            vo.setCodeNo(Long.parseLong(paraMap.get("code_no").toString()));
        }
        catch (NullPointerException ne) {
            vo.setCodeNo(0L);
        }
        CodeInfo chkvo = codeRepo.findByCodeNoAndUsedYn(vo.getCodeNo(),"Y");
        if (chkvo != null) {
            chkvo.setCodeNm(paraMap.get("code_nm").toString());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
        }
        else {
            chkvo = new CodeInfo();
            chkvo.setCodeNo(0L);
            chkvo.setCodeNm(paraMap.get("code_nm").toString());
            chkvo.setCodeBrief(chkvo.getCodeNm());
            chkvo.setCodeAlais(chkvo.getCodeNm());
            chkvo.setCodeSeq(0);
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
            chkvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setRegIp(paraMap.get("ipaddr").toString());
            chkvo.setRegDt(DateUtils.getCurrentBaseDateTime());
        }
        chkvo.setParCodeNo((long) 1900);
        chkvo.setUsedYn("Y");
        chkvo.setModableYn("Y");
        vo = codeRepo.save(chkvo);
        return vo;
    }

    @Override
    public CodeInfo saveCode(Map<String, Object> paraMap) {
        CodeInfo vo = new CodeInfo();
        vo.setUsedYn("Y");
        vo.setParCodeNo(Long.parseLong(paraMap.get("par_code_no").toString()));

        try{
            vo.setCodeNo(Long.parseLong(paraMap.get("code_no").toString()));
        }catch(NullPointerException ne){
            vo.setCodeNo(0L);
        }
        vo.setCodeNm(paraMap.get("code_nm").toString());
        try {
            vo.setCodeBrief(paraMap.get("code_brief").toString());
        }
        catch (NullPointerException ne) {
            vo.setCodeBrief(paraMap.get("code_nm").toString());
        }
        try {
            vo.setCodeAlais(paraMap.get("code_alais").toString());
        }
        catch (NullPointerException ne) {
            vo.setCodeAlais(paraMap.get("code_nm").toString());
        }

        vo.setModableYn("Y");
        vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        vo.setModIp(paraMap.get("ipaddr").toString());
        vo.setModDt(DateUtils.getCurrentDate());
        CodeInfo chkvo = codeRepo.findByCodeNoAndUsedYn(vo.getCodeNo(), "Y");
        if (chkvo != null) {
            vo.setRegId(chkvo.getRegId());
            vo.setRegIp(chkvo.getRegIp());
            vo.setRegDt(chkvo.getRegDt());
            vo.setCodeSeq(chkvo.getCodeSeq());
        }
        else {
            vo.setCodeNo(0L);
            vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setRegIp(paraMap.get("ipaddr").toString());
            vo.setRegDt(DateUtils.getCurrentDate());

            try {
                vo.setCodeSeq(Integer.parseInt(paraMap.get("code_seq").toString()));
            }
            catch (NullPointerException ne) {
                Map<String, Object> cntMap = new HashMap<String,Object>();
                cntMap.put("parCodeNo", Long.parseLong(paraMap.get("par_code_no").toString()));
                vo.setCodeSeq(mapper.getCodeListCount(cntMap));
            }
        }

        vo = codeRepo.save(vo);

        return vo;
    }

    @Override
    public List<Object> getCodeTree(Map<String, Object> paraMap){
        //String tag = "mgr.am.aadm.selectPageGradeMappingList==>";
        //Map < String, Object > state = new HashMap<String,Object>();

        elist = mapper.getCodeTree(paraMap);
        List<Object> children = getChildren(0); //eaMenuList(node) 생성
        List < Object > treeData =  new ArrayList < Object > ();
        root.put("id", "0");
        root.put("text", "MES 공통코드");

        LinkedHashMap <String,Object> state  = new LinkedHashMap < String, Object > ();
        state.put("expanded",true);
        root.put("state",state);

        root.put("children",children );
        treeData.add(root);
        //state.put("opened",  "true");
        //root.put("state", state);
        return treeData;
    }
    private List < Object > getChildren( int idx) {
        String leafYn = "";
        String pid = "";
        String menuNo = "";
        String menuNm = "";
        Map<String,Object> emap = null;
        int nReadDepth = 0;

        List < Object > menuList0 =  new ArrayList < Object > ();
        List < Object > menuList1 =  new ArrayList < Object > ();
        List < Object > menuList2 =  new ArrayList < Object > ();
        List < Object > menuList3 =  new ArrayList < Object > ();
        List < Object > menuList4 =  new ArrayList < Object > ();
        List < Object > menuList5 =  new ArrayList < Object > ();
        List < Object > menuList6 =  new ArrayList < Object > ();

        int nSaveDepth = 4; //메뉴최대깊이 : 임시로 상수처리하고 있음.
        int nJdx = 0;
        Map < String, Object > state = new HashMap<String,Object>();
        while (idx < elist.size() ) {

            emap = elist.get(idx);
            menuNo = String.valueOf(emap.get("codeNo"));
            menuNm = String.valueOf(emap.get("codeNm"));
            pid = String.valueOf(emap.get("parCodeNo"));
            nReadDepth = (int) emap.get("level");
            log.info(nReadDepth+"nReadDepth");
            LinkedHashMap <String,Object> node  = new LinkedHashMap < String, Object > ();
            List < Object > etree = null;
//            log.info(emap+"whatThis");
            if (nReadDepth != nSaveDepth  ) {

                if (nReadDepth == 0) {
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList1.size()) {
                        log.info(menuList1+"menuList1");
                        chkmap = (Map<String, Object>) menuList0.get(menuList0.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList1.get(nJdx);
                        Long chkParentId = Long.parseLong(chkmap.get("id").toString());
                        Long parent = Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add( menuList1.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList0.add(node);
                }
                else if (nReadDepth == 1) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    if (menuNm.equals("게시관리")) {

                    }
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList2.size()) {
                        log.info(menuList2+"menuList2");
                        chkmap = (Map<String, Object>) menuList1.get(menuList1.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList2.get(nJdx);
                        log.info("chkmap"+chkmap);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList2.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList1.add(node);
                }
                else if (nReadDepth == 2) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList3.size()) {
                        log.info(menuList3+"menuList3");
                        chkmap = (Map<String, Object>) menuList2.get(menuList2.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList3.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        log.info(chkmap + "chkmap");
                        if (chkParentId == parent) {
                            etree.add(menuList3.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList2.add(node);
                }
                else if (nReadDepth == 3) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList4.size()) {
                        log.info(menuList4+"menuList4");
                        chkmap = (Map<String, Object>) menuList3.get(menuList3.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList4.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList4.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList3.add(node);
                }
                else if (nReadDepth == 4) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList5.size()) {
                        log.info(menuList5+"menuList5");
                        chkmap = (Map<String, Object>) menuList4.get(menuList4.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList5.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList5.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList4.add(node);
                }
                else if (nReadDepth == 5) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList6.size()) {
                        log.info(menuList6+"menuList6");
                        chkmap = (Map<String, Object>) menuList5.get(menuList5.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList6.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList6.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList5.add(node);
                }
            } else {
                log.info("idx!@!@#!#@"+idx);
                node.put("pid", pid);
                node.put("id", menuNo);
                node.put("text", menuNm);
                menuList6.add(node);
            }
            ++idx;
        }


        emap = elist.get(idx - 1);
        menuNo = String.valueOf(emap.get("dept_no"));
        menuNm = String.valueOf(emap.get("dept_nm"));
        pid = String.valueOf(emap.get("par_dept_no"));
        nReadDepth = 3;

        LinkedHashMap <String,Object> node  = new LinkedHashMap < String, Object > ();
        List < Object > etree = null;
        while (nReadDepth >= 1) {
            if (nReadDepth == 0) {
                node.put("id", menuNo);
                node.put("text", menuNm);
                nJdx = -1;
                etree = new ArrayList<Object>();
                Map<String, Object> chkmap = null;
                while (++nJdx < menuList1.size()) {
                    log.info(menuList1 + "menuList1");

                    chkmap = (Map<String, Object>) menuList0.get(menuList0.size() - 1);
                    Map<String, Object> mapInfo = (Map<String, Object>) menuList1.get(nJdx);
                    Long chkParentId = Long.parseLong(chkmap.get("id").toString());
                    Long parent = Long.parseLong(mapInfo.get("pid").toString());
                    if (chkParentId == parent) {
                        etree.add(menuList1.get(nJdx));
                    }
                }
                if (etree.size() > 0) {
                    chkmap.put("children", etree);
                }
//                menuList0.add(node);
            } else if (nReadDepth == 1) {
                node.put("pid", pid);
                node.put("id", menuNo);
                node.put("text", menuNm);
                if (menuNm.equals("게시관리")) {

                }
                nJdx = -1;
                etree = new ArrayList<Object>();
                Map<String, Object> chkmap = null;
                while (++nJdx < menuList2.size()) {
                    log.info(menuList2 + "menuList2");
                    chkmap = (Map<String, Object>) menuList1.get(menuList1.size() - 1);
                    Map<String, Object> mapInfo = (Map<String, Object>) menuList2.get(nJdx);
                    int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                    int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                    if (chkParentId == parent) {
//
                        etree.add(menuList2.get(nJdx));
                    }

                }

                if (etree.size() > 0) {
                    chkmap.put("children", etree);

                }
//                menuList1.add(node);

            } else if (nReadDepth == 2) {
                node.put("pid", pid);
                node.put("id", menuNo);
                node.put("text", menuNm);
                nJdx = -1;
                etree = new ArrayList<Object>();
                Map<String, Object> chkmap = null;
                while (++nJdx < menuList3.size()) {
                    log.info(menuList3 + "menuList3");
                    chkmap = (Map<String, Object>) menuList2.get(menuList2.size() - 1);
                    Map<String, Object> mapInfo = (Map<String, Object>) menuList3.get(nJdx);
                    int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                    int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                    log.info(chkmap + "chkmap");
                    if (chkParentId == parent) {
                        etree.add(menuList3.get(nJdx));
                    }
                }
                if (etree.size() > 0) {
                    chkmap.put("children", etree);
                }
//                menuList2.add(node);
            } else if (nReadDepth == 3) {
                node.put("pid", pid);
                node.put("id", menuNo);
                node.put("text", menuNm);
                nJdx = -1;
                etree = new ArrayList<Object>();
                Map<String, Object> chkmap = null;
                while (++nJdx < menuList4.size()) {
                    log.info(menuList4 + "menuList4");
                    chkmap = (Map<String, Object>) menuList3.get(menuList3.size() - 1);
                    Map<String, Object> mapInfo = (Map<String, Object>) menuList4.get(nJdx);
                    int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                    int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                    if (chkParentId == parent) {
                        etree.add(menuList4.get(nJdx));
                    }
                }
                if (etree.size() > 0) {
                    chkmap.put("children", etree);
                }
//                menuList3.add(node);
            } else if (nReadDepth == 4) {
                node.put("pid", pid);
                node.put("id", menuNo);
                node.put("text", menuNm);
                nJdx = -1;
                etree = new ArrayList<Object>();
                Map<String, Object> chkmap = null;
                while (++nJdx < menuList5.size()) {
                    log.info(menuList5 + "menuList5");
                    chkmap = (Map<String, Object>) menuList4.get(menuList4.size() - 1);
                    Map<String, Object> mapInfo = (Map<String, Object>) menuList5.get(nJdx);
                    int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                    int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                    if (chkParentId == parent) {
                        etree.add(menuList5.get(nJdx));
                    }
                }
                if (etree.size() > 0) {
                    chkmap.put("children", etree);
                }
//                menuList4.add(node);
            } else if (nReadDepth == 5) {
                node.put("pid", pid);
                node.put("id", menuNo);
                node.put("text", menuNm);
                nJdx = -1;
                etree = new ArrayList<Object>();
                Map<String, Object> chkmap = null;
                while (++nJdx < menuList6.size()) {
                    log.info(menuList6 + "menuList6");
                    chkmap = (Map<String, Object>) menuList5.get(menuList5.size() - 1);
                    Map<String, Object> mapInfo = (Map<String, Object>) menuList6.get(nJdx);
                    int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                    int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                    if (chkParentId == parent) {
                        etree.add(menuList6.get(nJdx));
                    }
                }
                if (etree.size() > 0) {
                    chkmap.put("children", etree);
                }
//                menuList5.add(node);
            }
            nReadDepth--;
        }

        return menuList1;
    }

}
