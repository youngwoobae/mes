package daedan.mes.sysmenu.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@NoArgsConstructor
public class AuthUserMenu {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="auth_user_menu_no",nullable = false)
    private Long authUserMenuNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*사용자별메뉴접근접근관리번호*/
    @Column(name="auth_user_no",nullable = false)
    private Long authUserNo;

    /*시스템메뉴번호*/
    @Column(name="sys_menu_no",nullable = false)
    private Long sysMenuNo;

    /*등록가능여부*/
    @Column(name="apnd_yn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String apndYn;

    /*수정가능여부*/
    @Column(name="save_yn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String saveYn;

    /*삭제가능여부*/
    @Column(name="drop_yn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String dropYn;

    /*정보다운로드*/
    @Column(name="dnload_yn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String dnloadYn;

    /*정보업로드*/
    @Column(name="upload_yn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String uploadYn;

    /*리스트출력*/
    @Column(name="list_prt_yn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String listPrtYn;

    /*정보출력*/
    @Column(name="info_prt_yn",nullable = false, columnDefinition = "char(1) default 'N'")
    private String infoPrtYn;

    /*개인정보접근레벨 0:없음,1:메일주소,2:전화번호,9:전체*/
    @Column(name="per_info_lvl",nullable = false, columnDefinition = "int default 0")
    private Integer perInfoLvl;

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
}
