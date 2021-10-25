package daedan.mes.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

}
