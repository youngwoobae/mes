package daedan.mes.role.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class RoleInfo {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_no",nullable = false, columnDefinition = "numeric")
    private Long roleNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*역할코드*/
    @Column(name="role_code",nullable = false, columnDefinition = "numeric" )
    private Long roleCode;

    /*메뉴번호*/
    @Column(name="sys_menu_no",nullable = false, columnDefinition = "numeric" )
    private Long sysMenuNo;

    /*사용여부*/
    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    /*등록id*/
    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;
    /*등록ip*/
    @Column(name="reg_ip", length = 20)
    private String regIp;
    /*등록일자*/
    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    /*수정ip*/
    @Column(name="mod_ip",nullable = false, length = 20)
    private String modIp;
    /*수정id*/
    @Column(name="mod_id",nullable = false, columnDefinition = "numeric")
    private Long modId;

    /*수정일자*/
    @Column(name="mod_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    public Date getRegDt(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return  new Timestamp(date.getTime());
    }

    /*수정일자*/
    public Date getModDt(){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return  new Timestamp(date.getTime());
    }

}
