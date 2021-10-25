package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MakeIndc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indc_no",nullable = false, columnDefinition = "numeric")
    private Long indcNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*부모 지시번호*/
    @Column(name="par_indc_no", nullable = false, columnDefinition = "numeric default 0")
    private Long parIndcNo;

    /*지시일자*/
    @Column(name="indc_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indcDt;

    /*상품번호*/
    @Column(name="prod_no", columnDefinition = "numeric")
    private Long prodNo;

    /*가동율:2021.05.19추가*/
    @Column(name="oper_rt" , columnDefinition = "numeric default 0.0", precision=7, scale=3)
    private Float operRt;

    /*충진수율:2021.05.04 추가*/
    @Column(name="fill_yield" , columnDefinition = "numeric default 100.0", precision=7, scale=3)
    private Float fillYield;

    /*조정충진수율:2021.05.04 추가*/
    @Column(name="ctl_fill_yield" , columnDefinition = "numeric default 100.0", precision=7, scale=3)
    private Float ctlFillYield;

    /*운영수율:2021.05.19추가*/
    @Column(name="real_yield" , columnDefinition = "numeric default 100.0", precision=7, scale=3)
    private Float realYield;

    /*불량율:2021.05.19추가*/
    @Column(name="fault_rt" , columnDefinition = "numeric default 0.0", precision=7, scale=3)
    private Float faultRt;

    /*공정코드*/
    @Column(name="proc_cd",nullable = false, columnDefinition = "numeric")
    private Long procCd;

    /*생산시작일자*/
    @Column(name="make_fr_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeFrDt;

    /*생산종료일자*/
    @Column(name="make_to_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeToDt;

    /*자재요청일자*/
    @Column(name="matr_req_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date matrReqDt;

    /*지시중량:(proc_info.proc_unit에 따라 수량이 될수도 있음(단위포장 등)에 주의 할 것.*/
    @Column(name="indc_wgt", columnDefinition = "numeric")
    private Float indcWgt;

    @Column(name="indc_qty", columnDefinition = "numeric")
    private Float indcQty;


    /*최대생산캐퍼(수량):*/
    @Column(name="max_make_qty",nullable = false, precision=8, scale=2, columnDefinition = "numeric default 0")
    private Float maxMakeQty;


    /*최대생산캐퍼(중량):*/
    @Column(name="max_make_wgt",nullable = false, precision=8, scale=2 , columnDefinition = "numeric default 0")
    private Float maxMakeWgt;

    /*생산단위코드*/
    @Column(name="make_unit",nullable = false, columnDefinition = "numeric")
    private Long makeUnit;


    /*상태코드 */
    @Column(name="stat_cd",nullable = false, columnDefinition = "numeric default 501")
    private Long statCd;

    /*작업지시 상태*/
    @Column(name="indc_sts", columnDefinition = "numeric")
    private Long indcSts;

    /*작업지시 경로*/
    @Column(name="indc_tp", columnDefinition = "numeric")
    private Long indcTp;

    /*지시내용*/
    @Column(name="indc_cont", length = 4000)
    private String indcCont;

    /*연관주문번호*/
    @Column(name="ord_no", columnDefinition = "numeric")
    private Long ordNo;

    @Column(name="manual_yn" ,  columnDefinition = "char default 'Y'")
    private String manualYn;

    // AddOn By KMJ At 21.08.05 22:10-생산완료여부
    @Column(name="clos_yn", length = 1 , columnDefinition = "char default 'N'")
    private String closYn;


    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
    private Long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip", length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    private String buf;
    private int idxNo;


}
