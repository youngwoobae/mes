package daedan.mes.purs.domain;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.matr.domain.MatrAttr;
import daedan.mes.matr.domain.MatrInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor

public class PursMatr {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="purs_matr_no",nullable = false)
    private Long pursMatrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="purs_no",nullable = false )
    private Long pursNo;

    /*구매처번호*/
    @Column(name="cmpy_no",columnDefinition = "bigint default 0")
    private Long cmpyNo;


    /*원자재번호*/
    @Column(name="matr_no",nullable = false)
    private Long matrNo;


    @Column(name="purs_qty",nullable = false, precision=10, scale=2)
    private Float pursQty;

    @Column(name="purs_unit",nullable = false)
    private Long pursUnit;

    @Column(name="purs_amt", precision=10, scale=2)
    private Long pursAmt;

    @Column(name="wh_no",columnDefinition = "bigint default 0")
    private Long whNo;

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

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="purs_sts",nullable = false)
    private Long pursSts;

    public Date getRegDt(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return  new Timestamp(date.getTime());
    }

    public Date getModDt(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return  new Timestamp(date.getTime());
    }
}
