package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrStk {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="matr_stk_no",nullable = false, columnDefinition = "numeric")
    private Long matrStkNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*자재번호*/
    @Column(name="matr_no",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*재고수량*/
    @Column(name="stk_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float stkQty;

    /*재고상태코드:입고,소분,출고,분실,폐기*/
    @Column(name="stat_cd", columnDefinition = "numeric")
    private Long statCd;

    /*상태전이일자*/
    @Column(name="stat_trf_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date statTrfDt;

    /*유통기한*/
    @Column(name="valid_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date validDt;

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
