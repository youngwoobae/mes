package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor

public class ProdStkLot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="prod_lot_no",nullable = false, columnDefinition = "numeric")
    private Long prodLotNo;

    @Column(name="cust_no", columnDefinition = "numeric default 0")
    private Long custNo;

    /*상품번호*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric default 0 " )
    private Long whNo;

    /*적재일자(입고일자)*/
    @Column(name="stk_ut", nullable = false)
    private Long stkUt;


    /*재고수량*/
    @Column(name="stk_qty",nullable = false , precision=10, scale=2)
    private Float stkQty;


    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
    private Long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip", length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
}
