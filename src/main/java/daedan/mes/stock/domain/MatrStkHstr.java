package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrStkHstr {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chng_no", nullable = false, columnDefinition = "numeric")
    private Long chngNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*원료번호*/
    @Column(name="matr_no",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric default 0 " )
    private Long whNo;

    /*재고수량*/
    @Column(name="stk_qty",nullable = false , precision=10, scale=2)
    private Float stkQty;

    /*변경사유코드*/
    @Column(name="chng_resn",nullable = true)
    private Long chngResn;

    /*사용여부*/
    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
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
