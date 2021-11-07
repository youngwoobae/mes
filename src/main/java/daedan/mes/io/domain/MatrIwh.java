package daedan.mes.io.domain;

/*자재입고정보*/

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrIwh {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="iwh_no",nullable = false, columnDefinition = "numeric")
    private Long iwhNo;

    /*구매번호*/
    @Column(name="purs_no",nullable = false, columnDefinition = "numeric default 0")
    private Long pursNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*구매처번호*/
    @Column(name="cmpy_no",nullable = false, columnDefinition = "numeric default 0")
    private Long cmpyNo;

    /*자재번호*/
    @Column(name="matr_no",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*자재구매번호*/
    @Column(name="purs_matr_no", columnDefinition = "numeric default 0")
    private Long pursMatrNo;

    /*제조일자*/
    @Column(name="date_manufacture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateManufacture;

    /*검수자id*/
    @Column(name="inspEr", columnDefinition = "numeric default 0")
    private Long inspEr;

    /*파레트코드*/
    @Column(name="paltCd", columnDefinition = "numeric default 0")
    private Long paltCd;

    /*파레트수량*/
    @Column(name="paltQty", columnDefinition = "numeric default 0")
    private Integer paltQty;

    /*입고일자*/
    @Column(name="iwh_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwhDt;

    /*입고수량*/
    @Column(name="iwh_qty",nullable = false, precision=10, scale=2)
    private Float iwhQty;

    /*반품수량*/
    @Column(name="retn_qty",nullable = false, precision=10, scale=2,columnDefinition = "numeric default 0")
    private Float retnQty;

    /*출고수량*/
    @Column(name="owh_qty", columnDefinition = "numeric default 0")
    private Float owhQty;

    /*잔여수량*/
    @Column(name="remain_qty",columnDefinition = "numeric default 0")
    private Float remainQty;

    /*반품사유코드*/
    @Column(name="retn_resn", columnDefinition = "numeric default 0")
    private Long retnResn;

    /*유통기한*/
    @Column(name="valid_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date validDt;

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



}
