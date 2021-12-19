package daedan.mes.product.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProductIndc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_indc_no",nullable = false, columnDefinition = "numeric")
    private Long productIndcNo;

    @Column(name="cust_no", nullable = false, columnDefinition = "numeric default 0")
    private Long custNo;

    /*생산계획번호*/
    @Column(name="product_plan_no", nullable = false, columnDefinition = "numeric default 0")
    private Long productPlanNo;

    /*거래처번호*/
    @Column(name="cmpy_no", nullable = false, columnDefinition = "numeric default 0")
    private Long cmpyNo;

    /*상품번호*/
    @Column(name="prod_no", nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*생산시작일자*/
    @Column(name="make_fr_ut", nullable = false )
    private Long makeFrUt;

    /*생산종료일자*/
    @Column(name="make_to_ut", nullable = false)
    private Long makeToUt;

    /*지시량*/
    @Column(name="indc_qty", columnDefinition = "numeric default 0")
    private Float indcQty;

    /*상태코드 */
    @Column(name="stat_cd",nullable = false, columnDefinition = "numeric default 501")
    private Long statCd;

    /*지시내용*/
    @Column(name="indc_cont", length = 4000)
    private String indcCont;


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
