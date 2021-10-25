package daedan.mes.matr.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class BrnchInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="brnch_no",nullable = false)
    private Long brnchNo;

    /*모분류번호*/
    @Column(name="par_brnch_no",nullable = false)
    private Long parBrnchNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*분류명*/
    @Column(name="brnch_nm",nullable = false, length=80)
    private String brnchNm;

    @Column(name="reg_id",nullable = false)
    private Long regId;

    @Column(name="mod_id",nullable = false)
    private Long modId;

    @Column(name="reg_ip",nullable = false, length = 20)
    private String regIp;

    @Column(name="mod_ip",nullable = false, length = 20)
    private String modIp;

    @Column(name="reg_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

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

