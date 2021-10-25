package daedan.mes.ord.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class OrdProd {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ord_prod_no",nullable = false, columnDefinition = "numeric")
    private Long ordProdNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @JoinColumn(name="ordNo",nullable = false, columnDefinition = "numeric")
    private Long ordNo;

    @Column(name="prodNo",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*자재구매번호*/
    @Column(name="purs_no")
    private Long pursNo;

    @Column(name="ordQty",nullable = false, precision=10, scale=2 )
    private Float ordQty;

    /*주문규격*/
    @Column(name="ordSz", length = 100 )
    private String ordSz;

    /*주문모델명*/
    @Column(name="ordModlNm" , length = 100 )
    private String ordModlNm;

    /*판매단위*/
    @Column(name="saleUnit",nullable = false, columnDefinition = "numeric"  )
    private Long saleUnit;

    /*단가*/
    @Column(name="unitAmt")
    private Integer unitAmt;

    /*포장단위수량*/
    @Column(name="qtyPerPkg",nullable = false ,  columnDefinition = "int default 0" )
    private Integer qtyPerPkg;

    @Column(name="dlvQty",nullable = true,columnDefinition = "int default 0" )
    private Float dlvQty;

    @Column(name="dlvDt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dlvDt;

    @Column(name="makeAbleYn", columnDefinition = "char default 'N'")
    private String makeAbleYn;


    @Column(name="owhChk", columnDefinition = "bool default false")
    private boolean owhChk;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;


    @Column(name="regId", columnDefinition = "numeric")
    private Long regId;

    @Column(name="modId", columnDefinition = "numeric")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;


}
