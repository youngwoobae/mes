package daedan.mes.bord.domain;

import daedan.mes.user.domain.IndsType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class BordInfo {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="bord_no",nullable = false)
    private Long bordNo;

    /*모게시번호*/
    @Column(name="par_bord_no",nullable = false )
    private Long parBordNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*게시유형*/
    @Column(name="bord_tp",nullable = false )
    private Long bordTp;

    /*게시제목*/
    @Column(name="bord_subj",nullable = false )
    private String bordSubj;

    /*처리우선순위*/
    @Enumerated(EnumType.STRING)
    @Column(name="prioTp",nullable = true, length = 10)
    private PrioType prioTp;

    /*게시내용*/
    @Column(name="bord_cont",nullable = false )
    private String bordCont;

    /*게시시작일자*/
    @Column(name="fr_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date frDt;

    /*게시종료일자*/
    @Column(name="to_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDt;

    /*첨부파일번호*/
    @Column(name="file_no" )
    private Long fileNo;

    /*등록id*/
    @Column(name="reg_id", length = 20)
    private Long regId;
    /*등록ip*/
    @Column(name="reg_ip", length = 20)
    private String regIp;
    /*등록일자*/
    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    /*수정ip*/
    @Column(name="mod_ip", length = 20)
    private String modIp;
    /*수정id*/
    @Column(name="mod_id", length = 20)
    private Long modId;

    /*수정일자*/
    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    /*사용여부*/
    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;


}
