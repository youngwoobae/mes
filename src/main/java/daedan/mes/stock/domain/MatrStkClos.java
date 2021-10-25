package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrStkClos {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "matrStkClosNo", nullable = false, columnDefinition = "numeric")
    private Long matrStkClosNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*마감일자*/
    @Column(name="closDt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date closDt;

    /*창고번호*/
    @Column(name="whNo", columnDefinition = "numeric")
    private Long whNo;

    /*자재번호*/
    @Column(name="matrNo",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*입고수량*/
    @Column(name="iwhQty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float iwhQty;

    /*출고수량*/
    @Column(name="owhQty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float owhQty;

    /*재고수량*/
    @Column(name="stkQty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float stkQty;

    /*재고상태*/
    @Column(name="statCd",  columnDefinition = "numeric default 0")
    private Long statCd;

    /*사용구분*/
    @Column(name="usedYn",nullable = true, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="regId",nullable = true, columnDefinition = "numeric")
    private Long regId;

    @Column(name="modId",nullable = true, columnDefinition = "numeric")
    private Long modId;

    @Column(name="regIp",nullable = true, length = 20)
    private String regIp;

    @Column(name="modIp",nullable = true, length = 20)
    private String modIp;


    @Column(name="regDt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="modDt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
}