package daedan.mes.dept.service;

import daedan.mes.dept.mapper.DeptMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("deptService")
public class DeptServiceImpl implements  DeptService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private DeptMapper mapper;

    private List<Map<String,Object>> elist = null;
    LinkedHashMap <String,Object> root  = new LinkedHashMap < String, Object > ();

    @Override
    public List<Map<String, Object>> getComboDeptList(Map<String, Object> paraMap) {
        return mapper.getComboDeptList(paraMap);
    }
    public List<Map<String, Object>> getDeptList(Map<String, Object> paraMap) {
        return mapper.getDeptList(paraMap);
    }

    @Override
    public List<Object> deptTree(Map<String, Object> paraMap){
        String tag = "DetpService.deptTree==>";
        //Map < String, Object > state = new HashMap<String,Object>();
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        elist = mapper.getDeptList(paraMap);
        List<Object> children = getChildren(custNo,0); //eaMenuList(node) 생성
        List < Object > treeData =  new ArrayList< Object >();

        Long deptRoot =  custNo * 1000L;
        root.put("id", Long.toString(deptRoot));
        root.put("text", "MES 시스템");
        LinkedHashMap<String,Object> state  = new LinkedHashMap < String, Object > ();

        state.put("expanded",true);
        root.put("state",state);
        root.put("children",children );
        treeData.add(root);
        log.info(treeData+"treeDatatreeData");
        return treeData;
    }

    private List < Object > getChildren( Long custNo, int idx) {
        String pid = "";
        String menuNo = "";
        String menuNm = "";
        Map<String,Object> emap = null;
        int nReadDepth = 0;

        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("custNo",custNo);
        List < Object > menuList0 =  new ArrayList < Object > ();
        List < Object > menuList1 =  new ArrayList < Object > ();
        List < Object > menuList2 =  new ArrayList < Object > ();
        List < Object > menuList3 =  new ArrayList < Object > ();
        List < Object > menuList4 =  new ArrayList < Object > ();
        List < Object > menuList5 =  new ArrayList < Object > ();
        List < Object > menuList6 =  new ArrayList < Object > ();
        int nSaveDepth = mapper.getDeptListDepth(paraMap) + 1; //메뉴최대깊이 : 임시로 상수처리하고 있음.
        int nJdx = 0;
        Map < String, Object > state = new HashMap<String,Object>();
        while (idx < elist.size() ) {

            emap = elist.get(idx);
            menuNo = String.valueOf(emap.get("deptNo"));
            menuNm = String.valueOf(emap.get("deptNm"));
            pid = String.valueOf(emap.get("parDeptNo"));
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
        log.info(menuList1+"menuList1-First");
//        List<Object> testList = getChildrenLast(idx - 1);
//
        emap = elist.get(idx - 1);
        menuNo = String.valueOf(emap.get("deptNo"));
        menuNm = String.valueOf(emap.get("deptNm"));
        pid = String.valueOf(emap.get("parDeptNo"));
        nReadDepth = mapper.getDeptListDepth(paraMap) - 1;

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

        log.info(menuList1+"menuList1");

        return menuList1;
    }
}
