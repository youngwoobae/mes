package daedan.mes.ord.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class OrdInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ord_no",nullable = false, columnDefinition = "numeric")
    private Long ordNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="cmpy_no",nullable = false, columnDefinition = "numeric")
    private Long cmpyNo;

    /*주문유형:주문,수주(자체영업)*/
    @Column(name="ord_tp",nullable = false, columnDefinition = "numeric")
    private Long ordTp;

    /*주문경로:수주(자체영업)인 경우*/
    @Column(name="ord_path", columnDefinition = "numeric")
    private Long ordPath;

    @Column(name="ord_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ordDt;

    @Column(name="ord_sts",nullable = false)
    private Long ordSts;

    /*주문명*/
    @Column(name="ord_nm",nullable = false, length = 250)
    private String ordNm;

    //인수증번호
    @Column(name="take_no")
    private String takeNo;

    /*택배사*/
    @Column(name="trk_cmpy_no", columnDefinition = "numeric")
    private Long trkCmpyNo;

    /*운송장번호*/
    @Column(name="trk_no")
    private String trkNo;

    /*운송금액*/
    @Column(name="trk_amt")
    private Long trkAmt;

    /*납품요청일*/
    @Column(name="dlv_req_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dlvReqDt;

    /*납품일자*/
    @Column(name="dlv_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dlvDt;

    /*납품장소*/
    @Column(name="plc_no" ,nullable = false)
    private Long plcNo;

    /*자재구매번호 Remarked By KMJ AT 21.09.04 12:20 : OrdProd로 이전됨.
    @Column(name="purs_no")
    private Long pursNo;
    */

    /*주문형태*/
    @Column(name="ord_gbn")
    private Long ordGbn;

    /*단가*/
    @Column(name="unit_amt")
    private Integer unitAmt;

    /*부자재여부*/
    @Column(name="submatr_exist_yn", columnDefinition = "char default 'N'")
    private String submatrExistYn;


    @Column(name="reg_id")
    private Long regId;

    @Column(name="mod_id")
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

    /*파일번호*/
    @Column(name = "file_no",  columnDefinition = "numeric default 0")
    private Long fileNo;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;


}
