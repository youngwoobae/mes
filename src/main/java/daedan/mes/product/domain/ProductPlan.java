package daedan.mes.product.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProductPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_plan_no",nullable = false, columnDefinition = "numeric")
    private Long productPlanNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*생산걔획일자*/
    @Column(name = "plan_ut",nullable = false, columnDefinition = "numeric")
    private Long planUt;

    /*발송처번호목*/
    @Column(name = "cmpy_no", columnDefinition = "numeric")
    private Long cmpyNo;

    /*발송처에서 발송한 주문접수 번호*/
    @Column(name="ord_recv_no", columnDefinition = "numeric default 0")
    private Long ordRecvNo;


    /*생산계획품목*/
    @Column(name = "prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*생산걔획품목*/
    @Column(name = "plan_qty",nullable = false, columnDefinition = "numeric")
    private Integer planQty;

    /*상태코드 */
    @Column(name="stat_cd",nullable = false, columnDefinition = "numeric default 501")
    private Long statCd;

    /*사용여부*/
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
