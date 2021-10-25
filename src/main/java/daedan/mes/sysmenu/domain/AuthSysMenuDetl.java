package daedan.mes.sysmenu.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class AuthSysMenuDetl {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sys_menu_auth_detl_no",nullable = false)
    private Long sysMenuAuthDetlNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*시스템메뉴번호*/
    @Column(name="sys_menu_no",nullable = false, columnDefinition = "numeric default 0")
    private Long sysMenuNo;

    /*업로드가능여부*/
    @Column(name="upload_yn" , columnDefinition = "char(1) default 'N'")
    private String uploadYn;

    /*다운로드가능여부*/
    @Column(name="dnload_yn" , columnDefinition = "char(1) default 'N'")
    private String dnloadYn;

    /*인쇄가능여부*/
    @Column(name="print_yn" , columnDefinition = "char(1) default 'N'")
    private String printYn;

    /*자료관리관리가능여부*/
    @Column(name="mngr_yn" , columnDefinition = "char(1) default 'Y'")
    private String mngrYn;

}
