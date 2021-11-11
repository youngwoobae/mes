package daedan.mes.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class AuthUser {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="auth_user_no",nullable = false)
    private Long authUserNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*접근권한코드*/
    @Column(name="auth_cd",nullable = false, columnDefinition = "numeric default 0")
    private Long authCd;


    /*사용자ID*/
    @Column(name="user_Id",nullable = false, columnDefinition = "numeric default 0")
    private Long userId;

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
