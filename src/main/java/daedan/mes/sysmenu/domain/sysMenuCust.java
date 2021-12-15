package daedan.mes.sysmenu.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class sysMenuCust {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sys_menu_cust_no",nullable = false)
    private Long sysMenuCustNo;

    @Column(name="custNo",nullable = false ,columnDefinition = "numeric")
    private Long custNo;

    @Column(name="sys_menu_no",nullable = false,columnDefinition = "numeric")
    private Long sysMenuNo;


    @Column(name="disp_yn" , columnDefinition = "char(1) default 'Y'")
    private String dispYn;

    @Column(name="usedYn" , columnDefinition = "char(1) default 'Y'")
    private String usedYn;

}
