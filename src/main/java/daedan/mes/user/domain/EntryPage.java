package daedan.mes.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryPage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="mngr_no",nullable = false)
    private Long mngrNo;

    /*로그인후 최초접속 페이지(대쉬보드URL)*/
    @Column(name="fr_url", length = 50)
    private String frUrl;

    /*로그인 배경 이미지*/
    @Column(name="login_img", length = 50)
    private String loginImg;

    /*헤더에 표시될 회사명*/
    @Column(name="cmpy_nm", length = 50)
    private String cmpyNm;

    /*접속 IP : IP 주소에 따라 로그인 화면 및 초기화면 라우팅이 달라. */
    @Column(name="reg_ip", length = 50)
    private String regIp;

    @Column(name="used_yn",nullable = false,  columnDefinition = "char(1) default 'Y'")
    private String usedYn;

}
