package daedan.mes.user.domain;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.spot.domain.SpotEquip;
import daedan.mes.sysmenu.domain.SysMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class AccHstr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accNo")
    private Long accNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name = "userId" ,nullable = false ,columnDefinition = "numeric default 0")
    private Long userId;

    @Column(name="accDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accDt;


    //@OneToOne (fetch = FetchType.LAZY)
    //private SysMenu sysMenu;
    //@Column(name="sys_menu_no",nullable = false, columnDefinition = "numeric default 0",  updatable=false, insertable=false)
    //private Long sysMenuNo;

    @Column(name = "accUnixTime", columnDefinition = "numeric default 0")
    private Long accUnixTime;

    @Column(name = "sysMenuNo" ,nullable = false ,columnDefinition = "numeric default 0")
    private Long sysMenuNo;

}
