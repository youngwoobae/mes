package daedan.mes.stock.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.stock.service.StockService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/daedan/mes/stock")
public class StockController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private StockService stockService;

    @Autowired
    private CodeService codeService;


    @PostMapping(value="/saveWhInfo") //창고정보저장
    public Result saveWhInfo(@RequestBody Map<String, Object> pursMap, HttpServletRequest request, HttpSession session){
        Map<String,Object> paraMap = (Map<String, Object>) pursMap.get("whInfo");
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", pursMap.get("userId"));
        Result result = Result.successInstance();
        stockService.saveWhInfo(paraMap);
        return result;
    }

    @PostMapping(value="/dropWhInfo") //창고정보저장
    public Result dropWhInfo(@RequestBody Map<String, Object> paraMap, HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        stockService.dropWhInfo(paraMap);
        return result;
    }

    @PostMapping(value="/comboWh") //창고목록
    public Result comboWh(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(stockService.getComboWh(paraMap));
        return result;
    }

    //키오스크(태블릿)입고 화면 조건
    @PostMapping(value="/conditions221MatrIwh")
    public Result conditions221MatrIwh(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(stockService.getWhList(paraMap));
        return result;
    }
    //창고레이아웃 팝업 화면 조건
    @PostMapping(value="/conditionPopMatrWhLayout")
    public Result conditionPopMatrWhLayout(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getWhInfo(paraMap));
        return result;
    }
    //자재실사 화면조건
    @PostMapping(value="/conditionEmbRealStock")
    public Result conditionEmbRealStock(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Map<String,Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_lose_cd"))); //실자재고변경사유
        paraMap.put("selectStr","재고변경사유선택");
        rmap.put("comboChngResn",codeService.getComboCodeList(paraMap));
        rmap.put("comboWhNo",stockService.getComboWh(paraMap));
        result.setData(rmap);
        return result;
    }


    @PostMapping(value="/comboSaveTmpr") //보관온도
    public Result WhContitions(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Map<String, Object>  rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd")));
        rmap.put("comboSaveTmpr",codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }
    //창고목록
    @PostMapping(value="/whList") //창고목록
    public Result whList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(stockService.getWhList(paraMap));
        result.setTotalCount(stockService.getWhListCount(paraMap));
        return result;
    }

    @PostMapping(value="/whInfo") //창고정보
    public Result whInfo(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(stockService.getWhInfo(paraMap));
        return result;
    }

    @PostMapping(value="/matrPosList") //원부자재재고적재위치목록
    public Result matrPosList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getMatrPosList(paraMap));
        result.setTotalCount(stockService.getMatrPosListCount(paraMap));
        return result;
    }
    @PostMapping(value="/matrStkList") //원부자재재고적재목록
    public Result matrStkList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getMatrStkList(paraMap));
        result.setTotalCount(stockService.getMatrStkListCount(paraMap));
        return result;
    }
    @PostMapping(value="/matrPosInfo") //원부자재재고적재위치정보
    public Result matrPosInfo(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(stockService.getMatrPosInfo(paraMap));
        return result;
    }

    @PostMapping(value="/saveMatrPos") //원부자재재고적재위치정보저장
    public Result saveMatrPos(@RequestBody HashMap<String, Object> paraMap , HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        stockService.saveMatrPos(paraMap);
        return result;
    }
    //실사재고
    @PostMapping(value="/realStockList")
    public Result realStockList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getRealStockList(paraMap));
        result.setTotalCount(stockService.getRealStockListCount(paraMap));
        return result;
    }
    //실사재고
    @PostMapping(value="/realStockHstr")
    public Result realStockHstr(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getRealStockHstr(paraMap));
        result.setTotalCount(stockService.getRealStockHstrCount(paraMap));
        return result;
    }

    @PostMapping(value="/realStockSave") //원부자재 실사재고 저장
    public Result realStockSave(@RequestBody HashMap<String, Object> paraMap , HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        stockService.realStockSave(paraMap);

        return result;
    }

    @PostMapping(value="/saveProdStock") //상품재고조정
    public Result saveProdStock(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        stockService.saveProdStock(paraMap);
        return result;
    }


    //수불대장 -- 총괄장
    @PostMapping(value="/stkClosList")
    public Result stkClosList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getStkClosList(paraMap));
        result.setTotalCount(stockService.getStkClosListCount(paraMap));
        return result;
    }
    //수불대장 -- 제품
    @PostMapping(value="/stkProdClosList")
    public Result stkProdClosList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getProdStkClosList(paraMap));
        result.setTotalCount(stockService.getProdStkClosListCount(paraMap));
        return result;
    }
    //수불대장 -- 자제
    @PostMapping(value="/stkMatrClosList")
    public Result stkMatrClosList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getMatrStkClosList(paraMap));
        result.setTotalCount(stockService.getMatrStkClosListCount(paraMap));
        return result;
    }

    //수불대장 -- 총괄장 / 자재입고 상세 팝업
    @PostMapping(value="/stkClosMatrIwhDetlHstr")
    public Result stkClosMatrIwhDetlHstr(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getStkClosMatrIwhDetlHstr(paraMap));
        result.setTotalCount(stockService.getStkClosMatrIwhDetlHstrCount(paraMap));
        return result;
    }
    //수불대장 -- 총괄장 / 자재출고 상세 팝업
    @PostMapping(value="/stkClosMatrOwhDetlHstr")
    public Result stkClosMatrOwhDetlHstr(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getStkClosMatrOwhDetlHstr(paraMap));
        result.setTotalCount(stockService.getStkClosMatrOwhDetlHstrCount(paraMap));
        return result;
    }
    //수불대장 -- 총괄장 / 상품출고 상세 팝업
    @PostMapping(value="/stkClosProdIwhDetlHstr")
    public Result stkClosProdIwhDetlHstr(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getStkClosProdIwhDetlHstr(paraMap));
        result.setTotalCount(stockService.getStkClosProdIwhDetlHstrCount(paraMap));
        return result;
    }

    //수불대장 -- 총괄장 / 상품출고 상세 팝업
    @PostMapping(value="/stkClosProdOwhDetlHstr")
    public Result stkClosProdOwhDetlHstr(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(stockService.getStkClosProdOwhDetlHstr(paraMap));
        result.setTotalCount(stockService.getStkClosProdOwhDetlHstrCount(paraMap));
        return result;
    }




    //수불대장 -- 자재-사용중지로 파악됨 (2021.05.16)
    @PostMapping(value="/matrIoList")
    public Result matrIoList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getMatrIoList(paraMap));
        result.setTotalCount(stockService.getMatrIoListCount(paraMap));
        return result;
    }

    //수불대장 -- 상품-사용중지로 파악됨 (2021.05.16)
    @PostMapping(value="/prodIoList")
    public Result prodIoList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getProdIoList(paraMap));
        result.setTotalCount(stockService.getProdIoListCount(paraMap));
        return result;
    }

    //창고별 자재적재위치
    @PostMapping(value="/matrStockPosList")
    public Result matrStockPosList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        paraMap.put("serverIp",env.getProperty("server.ip"));
        paraMap.put("portNo",env.getProperty("server.port"));
        result.setData(stockService.getMatrStockPosList(paraMap));

        return result;
    }

    @PostMapping(value="/addOnStock")
    public Result addOnStock(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        paraMap.put("serverIp",env.getProperty("server.ip"));
        paraMap.put("portNo",env.getProperty("server.port"));
        stockService.addOnStock(paraMap);
        return result;
    }

    @PostMapping(value="/matrStockListAndLayout")
    public Result matrStockListAndLayout(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Result result = Result.successInstance();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        paraMap.put("serverIp",env.getProperty("server.ip"));
        paraMap.put("portNo",env.getProperty("server.port"));
        result.setData(stockService.getMatrStockListAndLayout(paraMap));

        return result;
    }

    @PostMapping(value ="/checkMatrIwhList")
    public Result getCheckMatrIwhListCount(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(stockService.getCheckMatrIwhListCount(paraMap));
        return result;
    }

    //테블릿 자재실사재고 -- 간단MES용
    @PostMapping(value = "/matrForRealStkList")
    public Result getMatrForRealStkList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(stockService.getMatrForRealStkList(paraMap));
        result.setTotalCount(stockService.getMatrForRealStkListCount(paraMap));
        return result;
    }

    @PostMapping(value = "/saveMatrRealStkList")
    public Result saveMatrRealStkList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        stockService.saveMatrRealStkList(paraMap);

        return result;
    }

    //테블릿 제품실사재고 -- 간단MES용
    @PostMapping(value = "/prodForRealStkList")
    public Result getProdForRealStkList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        result.setData(stockService.getProdForRealStkList(paraMap));
        result.setTotalCount(stockService.getProdForRealStkListCount(paraMap));
        return result;
    }

    @PostMapping(value = "/saveProdRealStkList")
    public Result saveProdRealStkList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        stockService.saveProdRealStkList(paraMap);

        return result;
    }

    @PostMapping(value="/dropMatrRelStk") //창고정보저장
    public Result dropMatrRelStk(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        stockService.dropMatrRelStk(paraMap);
        return result;
    }


    @PostMapping(value="/remakeDailyCloseData") //
    public Result remakeDailyCloseData(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        stockService.remakeDailyCloseData(paraMap);
        return result;
    }
}
