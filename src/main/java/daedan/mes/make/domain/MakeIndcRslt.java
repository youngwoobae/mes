package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MakeIndcRslt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indc_rslt_no",nullable = false, columnDefinition = "numeric")
    private Long indcRsltNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*생산관리시번호*/
    @Column(name="indc_no",nullable = false, columnDefinition = "numeric")
    private Long indcNo;

    /*생산일시*/
    @Column(name="make_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeDt;
    /*조정충진수율:2021.05.04 추가*/
    @Column(name="ctl_fill_yield" , columnDefinition = "numeric default 100.0", precision=7, scale=3)
    private Float ctlFillYield;

    /*운영수율:2021.05.19추가*/
    @Column(name="real_yield" , columnDefinition = "numeric default 100.0", precision=7, scale=3)
    private Float realYield;

    /*생산수량*/
    @Column(name="make_qty", precision=10, scale=2 , columnDefinition = "numeric default 0")
    private Float makeQty;

    /*조정생산량*/
    @Column(name="adj_make_qty", columnDefinition = "numeric default 0")
    private Float adjMakeQty;

    /*생산중량*/
    @Column(name="make_wgt", precision=10, scale=2 , columnDefinition = "numeric default 0")
    private Float makeWgt;

    /*조정중량*/
    @Column(name="adj_make_wgt", columnDefinition = "numeric default 0")
    private Float adjMakeWgt;

    /*투입인력*/
    @Column(name="used_mp", precision=10, columnDefinition = "numeric default 0")
    private Long usedMp;

    /*사용전력*/
    @Column(name="used_pwr", precision=10, scale=2 , columnDefinition = "numeric default 0")
    private Long usedPwr;

    /*사용유량*/
    @Column(name="used_flux", precision=10, scale=2 , columnDefinition = "numeric default 0")
    private Long usedFlux;

    /*금속검출수량*/
    @Column(name="metal_qty", columnDefinition = "numeric default 0")
    private Long metalQty;

    /*중량미달*/
    @Column(name="wgt_qty", columnDefinition = "int default 0")
    private Long wgtQty;

    /*포장불량*/
    @Column(name="pack_qty", columnDefinition = "int default 0")
    private Long packQty;

    /*배합불량*/
    @Column(name="szn_qty", columnDefinition = "int default 0")
    private Long sznQty;

    /*살균온도*/
    @Column(name="ster_tmpr", columnDefinition = "int default 0")
    private Float sterTmpr;

    /*살균시간*/
    @Column(name="ster_time", columnDefinition = "numeric default 0")
    private Long sterTime;



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

}
