package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrPos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="matr_pos_no",nullable = false, columnDefinition = "numeric")
    private Long matrPosNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="matr_no",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*입고쳐리순번*/
    @Column(name="iwh_seq",nullable = false, columnDefinition = "numeric")
    private Integer iwhSeq;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*적재수량*/
    @Column(name="matr_qty",nullable = false, columnDefinition = "numeric default 0" , precision=5, scale=2)
    private Float matrQty;

    /*위치:단*/
    @Column(name="stair_idx",nullable = false, columnDefinition = "numeric")
    private Integer stairIdx;

    /*위치:행*/
    @Column(name="row_idx",nullable = false, columnDefinition = "numeric")
    private Integer rowIdx;

    /*위치:열*/
    @Column(name="col_idx",nullable = false, columnDefinition = "numeric")
    private Integer colIdx;

    /*추출일자*/
    @Column(name="owh_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhDt;

    /*자재상태(정상출고,분실,파손...)*/
    @Column(name="matr_stat",nullable = false, columnDefinition = "numeric")
    private Long matrStat;

    //QR품목코드Image
    @Column(name="qr_code_img" )
    private byte[] qrCodeImg;

    //Bar품목코드Image
    @Column(name="bar_code_img")
    private byte[] barCodeImg;

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
