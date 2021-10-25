package daedan.mes.adm.service;

import daedan.mes.adm.mapper.AdmMapper;
import daedan.mes.bord.mapper.BordMapper;
import daedan.mes.common.service.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("admService")
public class AdmServiceImpl implements  AdmService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private AdmMapper mapper;

    private List<Map<String,Object>> elist = null;
    Map< String, Object > root = new LinkedHashMap< String, Object >();

    @Override
    public JSONObject getMenuList(Map<String, Object> cond)  {
        //String tag = "mgr.am.aadm.selectPageGradeMappingList==>";
        Map < String, Object > state = new HashMap<String,Object>();

        elist = mapper.getSysMenuList(cond);
        List<Object> children = getChildren(0); //eaMenuList(node) 생성
        root.put("text", "MES시스템");
        root.put("id", "0");
        root.put("children",children );
        state.put("opened",  "true");
        root.put("state", state);
        return StringUtil.mapToJson(root);
    }

    List < Object > eaTree0 =  new ArrayList < Object > ();
    List < Object > eaTree1 =  new ArrayList < Object > ();
    List < Object > eaTree2 =  new ArrayList < Object > ();
    List < Object > eaTree3 =  new ArrayList < Object > ();
    List < Object > eaTree4 =  new ArrayList< Object >();

    int nSaveDepth = 0;


    private List < Object > getChildren( int idx) {
        String leafYn = "";
        String prntMenuId = "";
        String menuId = "";
        String menuNm = "";
        Map<String,Object> emap = null;
        int nReadDepth = 0;

        List < Object > menu0 =  new ArrayList < Object > ();
        List < Object > menu1 =  new ArrayList < Object > ();
        List < Object > menu2 =  new ArrayList < Object > ();
        List < Object > menu3 =  new ArrayList < Object > ();
        List < Object > menu4 =  new ArrayList < Object > ();

        int nSaveDepth = 4; //메뉴최대깊이 : 임시로 상수처리하고 있음.
        int nJdx = 0;
        Map < String, Object > state = new HashMap<String,Object>();
        while (idx < elist.size() ) {
            emap = elist.get(idx);
            menuId = String.valueOf(emap.get("sys_menu_no"));
            menuNm = String.valueOf(emap.get("sys_menu_nm"));
            prntMenuId = String.valueOf(emap.get("par_sys_menu_no"));
            nReadDepth = (int) emap.get("level");
            LinkedHashMap <String,Object> node  = new LinkedHashMap < String, Object > ();
            List < Object > etree = null;
            //log.info(tag + "kkkkkkk1.nReadDepth =  " + nReadDepth   +  " : nSaveDepth = " + nSaveDepth + " : menuNm = "  + menuNm);//kill
            if (nReadDepth != nSaveDepth  ) {
                if (nReadDepth == 0) {
                    node.put("id", menuId);
                    node.put("text", menuNm);
                    //state.put("opened", (leafYn.equals("Y")) ? "false" : "true");
                    node.put("state", state);
                    nJdx = -1;
                    etree =  new ArrayList < Object > ();
                    while(++nJdx < menu1.size()) {
                        Map<String,Object> mapMenu =  (Map<String,Object>) menu1.get(nJdx);
                        String parent = (String) mapMenu.get("par_sys_menu_no");
                        if (menuId.equals(parent)) {
                            etree.add(mapMenu);
                        }
                    }
                    if (etree.size() > 0) {
                        node.put("children",etree);
                    }
                    menu0.add(node);
                }

                else if (nReadDepth == 1) {
                    node.put("prntMenuId", prntMenuId);
                    node.put("id", menuId);
                    node.put("text", menuNm);
                    //state.put("opened", (leafYn.equals("Y")) ? "false" : "true");
                    node.put("state", state);
                    nJdx = -1;
                    etree =  new ArrayList < Object > ();
                    while(++nJdx < menu2.size()) {
                        Map<String,Object> mapMenu =  (Map<String,Object>) menu2.get(nJdx);

                        String parent = (String) mapMenu.get("prntMenuId");
                        if (menuId.equals(parent)) {
                            etree.add(mapMenu);
                        }
                    }
                    if (etree.size() > 0) {
                        node.put("children",etree);
                    }
                    menu1.add(node);
                }

                else if (nReadDepth == 2) {
                    node.put("prntMenuId", prntMenuId);
                    node.put("id", menuId);
                    node.put("text", menuNm);
                   //state.put("opened", (leafYn.equals("Y")) ? "false" : "true");
                    node.put("state", state);
                    nJdx = -1;
                    etree =  new ArrayList < Object > ();
                    while(++nJdx < menu3.size()) {
                        Map<String,Object> mapMenu =  (Map<String,Object>) menu3.get(nJdx);
                        String parent = (String) mapMenu.get("prntMenuId");
                        if (menuId.equals(parent)) {
                            etree.add(mapMenu);
                        }
                    }
                    if (etree.size() > 0) {
                        node.put("children",etree);
                    }
                    menu2.add(node);
                }
            }
            else if (nReadDepth == 3) {
                node.put("prntMenuId", prntMenuId);
                node.put("id", menuId);
                node.put("text", menuNm);
                //state.put("opened", (leafYn.equals("Y")) ? "false" : "true");
                node.put("state", state);
                nJdx = -1;
                etree =  new ArrayList < Object > ();
                while(++nJdx < menu4.size()) {
                    Map<String,Object> mapMenu =  (Map<String,Object>) menu3.get(nJdx);
                    String parent = (String) mapMenu.get("prntMenuId");
                    if (menuId.equals(parent)) {
                        etree.add(mapMenu);
                    }
                }
                if (etree.size() > 0) {
                    node.put("children",etree);
                }
                menu3.add(node);
            }
            else {
                node.put("id", menuId);
                node.put("text", menuNm);
                node.put("prntMenuId", prntMenuId);
                state.put("opened", (leafYn.equals("Y")) ? "false" : "true");
                node.put("state", state);
                menu3.add(node);
            }
            ++idx;
        }
        return menu1;
    }
}
