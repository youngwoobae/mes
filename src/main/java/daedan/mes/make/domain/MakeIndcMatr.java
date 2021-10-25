package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MakeIndcMatr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indc_matr_no",nullable = false, columnDefinition = "numeric")
    private Long indcMatrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*생상지시번호*/
    @Column(name="indc_no",nullable = false, columnDefinition = "numeric")
    private Long indcNo;

    @Column(name="matr_no",nullable = false , columnDefinition = "numeric ")
    private Long matrNo;

    @Column(name="need_qty",nullable = false, columnDefinition = "numeric default 0" , precision=10, scale=4)
    private  Float needQty;

    /*자재출고여부*/
    @Column(name="matr_sts", length = 1, columnDefinition = "char default 'N'")
    private String matrSts;

    /*원료사입여부*/
    @Column(name="takeYn",nullable = false, length = 1, columnDefinition = "char default 'N'")
    private String takeYn;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

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

    @Column(name="recv_qty")
    private Float recvQty;


}
