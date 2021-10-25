package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class  ProdStk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="stk_no",nullable = false, columnDefinition = "numeric")
    private Long stkNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*상품번호*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric default 0 " )
    private Long whNo;

    /*적재일자*/
    @Column(name="stk_dt", nullable = false)
    private Date stkDt;

    /*상태전이일자*/
    @Column(name="stat_trf_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date statTrfDt;

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

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

}
