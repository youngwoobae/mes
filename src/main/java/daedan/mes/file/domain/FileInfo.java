package daedan.mes.file.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor

public class FileInfo {
    private static final long serialVersionUID = 1L;

    public  FileInfo(Long fileNo, Long saveSeq, Long custNo,  String usedYn, String tmpYn, MultipartFile multipartFile, Long userId, String ipaddr) {
        this.custNo = custNo;
        this.fileNo = fileNo;
        this.saveSeq = saveSeq;
        this.custNo = custNo;
        this.usedYn = usedYn;
        this.tmpYn = tmpYn;
        this.regId = userId;
        this.modId = userId;
        this.regIp = ipaddr;
        this.modIp = ipaddr;
        this.multipartFile = multipartFile;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="file_no",nullable = false)
    private Long fileNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="save_seq",nullable = false,columnDefinition = "bigint default 0" )
    private Long saveSeq;

    @Column(name="file_tp",nullable = false,columnDefinition = "bigint default 0" )
    private Long fileTp;

    @Column(name="file_len",nullable = false,columnDefinition = "bigint default 0" )
    private Long fileLen;

    @Column(name="org_file_nm",nullable = false,length = 100 )
    private String orgFileNm;

    @Column(name="acc_url",length = 200 )
    private String accUrl;

    @Column(name="save_file_nm",nullable = false,length = 100 )
    private String saveFileNm;

    @Transient
    MultipartFile multipartFile;

    @Column(name="reg_id",nullable = false)
    private Long regId;

    @Column(name="mod_id",nullable = false)
    private Long modId;

    @Column(name="reg_ip",nullable = false, length = 20)
    private String regIp;

    @Column(name="mod_ip",nullable = false, length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="tmp_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String tmpYn;

}
