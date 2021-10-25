package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProdStkClos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "prod_stk_clos_no", nullable = false, columnDefinition = "numeric")
    private Long prodStkClosNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*마감일자*/
    @Column(name="clos_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date closDt;

    /*창고번호*/
    @Column(name="wh_no", columnDefinition = "numeric")
    private Long whNo;

    /*상품번호*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*입고수량*/
    @Column(name="iwh_qty", precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float iwhQty;

    /*출고수량*/
    @Column(name="owh_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float owhQty;


    /*재고수량*/
    @Column(name="stk_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float stkQty;

    /*재고상태*/
    @Column(name="stat_cd",nullable = false, columnDefinition = "numeric")
    private Long statCd;

    /*사용구분*/
    @Column(name="used_yn",nullable = true, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id",nullable = true, columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id",nullable = true, columnDefinition = "numeric")
    private Long modId;

    @Column(name="reg_ip",nullable = true, length = 20)
    private String regIp;

    @Column(name="mod_ip",nullable = true, length = 20)
    private String modIp;

    @Column(name="reg_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

}