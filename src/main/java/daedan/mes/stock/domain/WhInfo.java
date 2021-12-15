package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class WhInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*창고명*/
    @Column(name="wh_nm",nullable = false, length = 100)
    private String whNm;

    /*창고위치*/
    @Column(name="wh_loc_seq", nullable = false)
    private Long whLocSeq;

    /*보관온도*/
    @Column(name="save_tmpr",nullable = false, length = 100)
    private Long saveTmpr;

    /*창고위치번호*/
    @Column(name="wh_loc_seq", columnDefinition = "numeric default 1")
    private Byte whLogSeq;

    /*창고유형:자재창고,상품창고*/
    @Column(name="wh_tp",nullable = false)
    private Long whTp;

    /*사용구분*/
    @Column(name="used_yn",nullable = true, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;


    /*최대적재행*/
    @Column(name="max_row",nullable = true)
    private Integer maxRow;

    /*최대적재열*/
    @Column(name="max_col",nullable = true)
    private Integer maxCol;

    /*최대적재단*/
    @Column(name="max_stair",nullable = true)
    private Integer maxStair;

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
