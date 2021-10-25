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
public class MatrCmpy {

    public MatrCmpy(Long cmpyNo, String usedYn) {
         this.cmpyNo = cmpyNo;
         this.usedYn = usedYn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="matr_cmpy_no",nullable = false, columnDefinition = "")
    private Long matrCmpyNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="matr_no",nullable = false )
    private Long matrNo;

    @Column(name="cmpy_no",nullable = false )
    private Long cmpyNo;


    @Column(name="default_yn",nullable = false, length = 1 , columnDefinition = "char default 'N'")
    private String defaultYn;

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
    /*
    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="matr_no")
    private MatrInfo matrInfo;

    public MatrInfo getMatrInfo() {
        return matrInfo;
    }

    public void setMatrInfo(MatrInfo matrInfo) {
        this.matrInfo = matrInfo;
    }
     */
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
