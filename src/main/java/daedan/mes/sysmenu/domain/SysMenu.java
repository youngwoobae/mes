package daedan.mes.sysmenu.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor

public class SysMenu {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sys_menu_no",nullable = false)
    private Long sysMenuNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="par_sys_menu_no",nullable = false, columnDefinition = "int default 0")
    private Long parSysMenuNo;

    @Column(name="sys_menu_nm",nullable = false, length = 50)
    private String sysMenuNm;

    /*간략메뉴명*/
    @Column(name="brfMenuNm", length = 20)
    private String brfMenuNm;

    @Column(name="sys_menu_ip", length = 20)
    private String sysNmenuIp;

    @Column(name="sys_menu_port", columnDefinition = "int default 0")
    private Integer sysNmenuPort;

    @Column(name="prtSeq", columnDefinition = "numeric default 0")
    private Long prtSeq;

    @Column(name="makeSeq", columnDefinition = "numeric default 0")
    private Integer makeSeq;


    @Column(name="sysMenuPath", length = 250)
    private String sysMenuPath;

    @Column(name="regId", columnDefinition = "numeric")
    private Long regId;

    @Column(name="modId", columnDefinition = "numeric")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;


    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
    /*첨단메뉴의 기준메뉴경로:10_Base ...*/
    @Column(name="basePath", length = 30)
    private String basePath;


    @Column(name="reqAuthYn", columnDefinition = "char(1) default 'Y'")
    private String reqAuthYn;

    @Column(name="disp_yn" , columnDefinition = "char(1) default 'Y'")
    private String dispYn;

    @Column(name="usedYn" , columnDefinition = "char(1) default 'Y'")
    private String usedYn;

    @Column(name="printYn" , columnDefinition = "char(1) default 'Y'")
    private String printYn;

    @Column(name="moblYn" , columnDefinition = "char(1) default 'N'")
    private String moblYn;


    /*등록가능여부*/
    @Column(name="apndYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String apndYn;

    /*수정가능여부*/
    @Column(name="saveYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String saveYn;

    /*삭제가능여부*/
    @Column(name="dropYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String dropYn;

    /*정보다운로드*/
    @Column(name="dnloadYn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String dnloadYn;

    /*정보업로드*/
    @Column(name="uploadYn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String uploadYn;

    /*리스트출력*/
    @Column(name="listPrtYn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String listPrtYn;

    /*정보출력*/
    @Column(name="infoPrtYn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String infoPrtYn;

    /*개인어보관리*/
    @Column(name="perInfoYn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String perInfoYn;
}
