package daedan.mes.sysmenu.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class AuthSysMenu {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="auth_sys_menu_no",nullable = false)
    private Long sysMenuAuthNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*시스템메뉴번호*/
    @Column(name="sys_menu_no",nullable = false, columnDefinition = "numeric default 0")
    private Long sysMenuNo;

    /*접근권한코드*/
    @Column(name="authCd",nullable = false, columnDefinition = "numeric default 0")
    private Long authCd;
}
