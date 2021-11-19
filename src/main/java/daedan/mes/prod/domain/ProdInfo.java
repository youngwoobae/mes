package daedan.mes.prod.domain;

import daedan.mes.ccp.domain.HeatLmtInfo;
import daedan.mes.prod.domain.ddkor.ProdAttr;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@NoArgsConstructor
public class ProdInfo {
    private static final long serialVersionUID = 1L;

    public ProdInfo(long prodNo) {
        this.prodNo = prodNo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="prodNo",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    @Column(name="custNo" , columnDefinition = "numeric")
    private Long custNo;

    @Column(name="prodNm", nullable = false, length = 250)
    private String prodNm;

    @Column(name="erpProdNm", length = 250)
    private String erpProdNm;

    /*상품코드*/
    @Column(name="prodCode", length = 20)
    private String prodCode;

    /*OEM,ODM 구분 (AddOn By KMJ At 21.10.19 - 하담푸드요청사항, 35) 주의:주문유형과 같이 사용하는 코드임.*/
    @Column(name="prodTp",nullable = false,  columnDefinition = "numeric default 35")
    private Long prodTp;

    /*OEM 상품주문업체 (AddOn By KMJ At 21.10.19 - 하담푸드요청사항) */
    @Column(name="cmpyNo", columnDefinition = "numeric")
    private Long cmpyNo;

    /*제조공정번호:작업지시의 생성작업과 연동되는 필드임.*/
    @Column(name="brnchNo", columnDefinition = "numeric")
    private Long brnchNo;

    /*CCP TYPE:: baseCode=2300 (살균,금속검출, 살균및 금속검출)*/
    @Column(name="ccpTp", columnDefinition = "numeric default 0" )
    private Long ccpTp;

    /*제품살균기준코드:Ccp 살균 리포트시 가열시간 및 가열온도 적부를 판가름하기 위해 사용됨 : baseCode=2100 (홍삼음료,액상차) :HeatLmtInfo와 연동*/
    @Column(name="heatTp", columnDefinition = "numeric default 0" )
    private Long heatTp;

    /*제품속성구분 - (흑삼농축, 홍삼농축, 인삼농축), 기타 구분*/
    @Column(name="prodAttrTp", columnDefinition = "numeric default 0" )
    private Long prodAttrTp;

    /*보고상품번호*/
    @Column(name="reptProdNo", length = 20)
    private String reptProdNo;

    /*상품분류*/
    @Column(name="prodBrnch" )
    private Long prodBrnch;

    /*보관온도코드*/
    @Column(name="saveTmpr" )
    private Long saveTmpr;

    /*제품형태*/
    @Column(name="prod_shape", columnDefinition = "numeric default 0" )
    private Long prodShape;

    /*규격*/
    @Column(name="sz", length = 100)
    private String sz;

    /*모델명*/
    @Column(name="modlNm", length = 100)
    private String modlNm;

    /*판매단위코드*/
    @Column(name="saleUnit", columnDefinition = "numeric" )
    private Long saleUnit;

    /*판매단위중량 (유지물산은 kg 기타 g임)*/
    @Column(name="saleUnitgt", columnDefinition = "numeric" )
    private Long saleUnitWgt;

    /*창고번호 : 제품적재창고 : 나중에 사용할 예정임.*/
    @Column(name="wh_no", columnDefinition = "numeric default 0")
    private Long whNo;

    /*관리단위 : 중량 or 질랑*/
    @Column(name="mngrUnit",  columnDefinition = "numeric default 1701"  ) //17101=중량
    private Long mngrUnit;

    // 관리중량 :
    @Column(name = "vol" ,nullable = false, columnDefinition = "numeric default 0" )
    private Float vol;

    //비중
    @Column(name = "spga" , columnDefinition = "numeric default 1.0", precision=7, scale=3)
    private Float spga;

    /*관리질량(ml)*/
    @Column(name="mess", columnDefinition = "numeric default 0")
    private Float mess;


    /*최저 산도*/
    @Column(name="minPh" , columnDefinition = "numeric default 7"  )
    private Float minPh;

    /*최대 산도*/
    @Column(name="maxPh" , columnDefinition = "numeric default 7"  )
    private Float maxPh;



    /*SET당수량*/
    @Column(name="qtyPerPkg" , columnDefinition = "int default 1")
    private Integer qtyPerPkg;

    /*단가*/
    @Column(name="unitAmt")
    private Integer unitAmt;

    /*제품 bom 레벨*/
    @Column(name="bomLvl" ,nullable = false , columnDefinition = "numeric default 1")
    private Long bomLvl;

    /*상품이미지*/
    @Column(name="fileNo", columnDefinition = "numeric")
    private Long fileNo;

    //유효기간
    @Column(name="validTerm", columnDefinition = "int" )
    private Integer validTerm;

    //살균속성(온도,시간)
    @OneToOne (fetch = FetchType.LAZY, optional=true)
    private HeatLmtInfo heatLmtInfo;

    //제품속성(연근,진세노사이드,브릭스,고형분,점도,색도등 : 대동고려삼..)
    @OneToOne (fetch = FetchType.LAZY, optional=true)
    private ProdAttr prodAttr;

    //공정내용
    @Column(name="prod_cont", length = 4000)
    private String prodCont;

    @Column(name="regId", columnDefinition = "numeric")
    private Long regId;

    @Column(name="modId", columnDefinition = "numeric")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;


}
