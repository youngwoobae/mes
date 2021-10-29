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
    @Column(name="indc_no",nullable = false, columnDefinition = "numeric")
    private Long indcNo;

    /*출고요청일자*/
    @Column(name="owh_req_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhReqDt;

    /*출고요청수량*/
    @Column(name="owh_req_qty" ,nullable = false, precision=10, scale=2)
    private Float owhReqQty;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*출고일자*/
    @Column(name="owh_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhDt;

    /*출고수량*/
    @Column(name="owh_qty",nullable = false, precision=10, scale=2,columnDefinition = "numeric default 0" )
    private Float owhQty;

    /*출고단위*/
    @Column(name="owh_unit",nullable = false, columnDefinition = "numeric")
    private Long owhUnit;

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

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    /*검수자id*/
    @Column(name="inspEr", columnDefinition = "numeric default 0")
    private Long inspEr;

    /*파레트코드*/
    @Column(name="paltCd", columnDefinition = "numeric default 0")
    private Long paltCd;

}
