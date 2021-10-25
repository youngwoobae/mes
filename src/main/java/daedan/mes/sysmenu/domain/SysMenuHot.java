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
public class SysMenuHot {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="mngr_no",nullable = false)
    private Long MngrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="sys_menu_no",nullable = false)
    private Long sysMenuNo;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name="disp_seq", nullable = false, columnDefinition = "int default 0")
    private long dispSeq;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="used_yn" , columnDefinition = "char(1) default 'Y'")
    private String usedYn;


}
