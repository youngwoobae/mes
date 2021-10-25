package daedan.mes.cmpy.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class CmpyUser {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cmpy_user_id",nullable = false)
    private long cmpyUserId;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*성명*/
    @Column(name="user_nm",nullable = false, length = 20)
    private String userNm;

    /*메일주소*/
    @Column(name="mail_addr",nullable = false, length = 50)
    private String mailAddr;

    /*이동전화*/
    @Column(name="cell_no",nullable = false, length = 20)
    private String cellNo;

    /*직위*/
    @Column(name="post_nm", length = 20)
    private String postNm;


    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id")
    private long regId;

    @Column(name="mod_id")
    private long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip" ,length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;


    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

   // @ManyToOne( fetch=FetchType.LAZY)
   // @JoinColumn(name="cmpyNo")
   // private CmpyInfo cmpyInfo;

   // public CmpyInfo getCmpyInfo() {
    //    return cmpyInfo;
   // }
   // public void  setCmpyInfo(CmpyInfo cmpyInfo) {
    //    this.cmpyInfo = cmpyInfo;
   // }
}
