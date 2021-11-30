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
    @Column(name = "chngNo", nullable = false, columnDefinition = "numeric")
    private Long chngNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*원료번호*/
    @Column(name="matrNo",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*창고번호*/
    @Column(name="whNo",nullable = false, columnDefinition = "numeric default 0 " )
    private Long whNo;

    /*재고수량*/
    @Column(name="stkQty",nullable = false , precision=10, scale=2)
    private Float stkQty;

    /*변경사유코드*/
    @Column(name="chngResn",nullable = true)
    private Long chngResn;

    /*사용여부*/
    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
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
