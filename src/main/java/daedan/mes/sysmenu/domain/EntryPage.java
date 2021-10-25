package daedan.mes.sysmenu.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class EntryPage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mngr_no",nullable = false, columnDefinition = "numeric")
    private Long mngrNo;

    @Column(name="fr_url",nullable = false, length = 100)
    private String frUrl;

    @Column(name="login_img",nullable = false, length = 50)
    private String loginImg;

    @Column(name="cmpy_nm",nullable = false, length = 50)
    private String cmpyNm;

    @Column(name="cust_no",nullable = true, length = 20)
    private Long custNo;

    @Column(name="reg_ip",nullable = true, length = 20)
    private String regIp;

    @Column(name="used_yn",nullable = true, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;
}
