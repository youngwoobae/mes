package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class IndcInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indc_no",nullable = false, columnDefinition = "numeric")
    private Long indcNo;

    @Column(name="cust_no", nullable = false, columnDefinition = "numeric default 0")
    private Long custNo;

    /*지시일자*/
    @Column(name="indc_dt" ,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date indcDt;

    /*상품번호*/
    @Column(name="prod_no", nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*생산시작일자*/
    @Column(name="make_fr_dt" , nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeFrDt;

    /*생산종료일자*/
    @Column(name="make_to_dt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeToDt;

    /*지시량*/
    @Column(name="indc_qty", columnDefinition = "numeric default 0")
    private Float indcQty;

    /*상태코드 */
    @Column(name="stat_cd",nullable = false, columnDefinition = "numeric default 501")
    private Long statCd;

    /*지시내용*/
    @Column(name="indc_cont", length = 4000)
    private String indcCont;

    /*계획연계번호*/
    @Column(name="make_plan_no", columnDefinition = "numeric")
    private Long makePlanNo;

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

}
