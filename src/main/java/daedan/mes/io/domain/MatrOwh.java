package daedan.mes.io.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrOwh {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="owh_no",nullable = false, columnDefinition = "numeric")
    private Long owhNo;

    /*자재번호*/
    @Column(name="matr_no",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*생산지시번호*/
    @Column(name="indcNo",nullable = false, columnDefinition = "numeric")
    private Long indcNo;

    /*출고요청일자*/
    @Column(name="owhReqDt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhReqDt;

    /*출고요청수량*/
    @Column(name="owhReqQty" ,nullable = false, precision=10, scale=2)
    private Float owhReqQty;

    /*창고번호*/
    @Column(name="whNo",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*출고일자*/
    @Column(name="owhDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhDt;

    /*출고수량*/
    @Column(name="owhQty",nullable = false, precision=10, scale=2,columnDefinition = "numeric default 0" )
    private Float owhQty;

    /*출고단위*/
    @Column(name="owhUnit",nullable = false, columnDefinition = "numeric")
    private Long owhUnit;

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

    /*비고*/
    @Column(name="rmk" , length = 2048)
    private String rmk;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    /*검수자id*/
    @Column(name="inspEr", columnDefinition = "numeric default 0")
    private Long inspEr;

    /*파레트코드*/
    @Column(name="paltCd", columnDefinition = "numeric default 0")
    private Long paltCd;

}
