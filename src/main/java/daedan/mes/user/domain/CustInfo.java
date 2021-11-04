package daedan.mes.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class CustInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "custNo")
    private Long custNo;

    /* custNo에 따라 로그인 화면 및 초기화면 라우팅이 달라짐
    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;
    */

    /*로그인후 최초접속 페이지(대쉬보드URL)*/
    @Column(name="frUrl", length = 50)
    private String frUrl;

    /*로그인 배경 이미지*/
    @Column(name="loginImg", length = 50)
    private String loginImg;

    @Column(name = "cmpyNm", length = 250)
    private String cmpyNm;

    @Column(name = "chatPort", columnDefinition = "int default 19080")
    private Integer chatPort;

    /*업체별라이센스코드*/
    @Column(name="lcnsCd", length = 250)
    private String lcnsCd;

    /*BOM관리여부*/
    @Column(name = "bomYn", columnDefinition = "char(1) default 'N'")
    private String bomYn;

    /*포장단위관리여부 : 대동고려삼만 'Y'*/
    @Column(name = "pkgUnitYn", columnDefinition = "char(1) default 'N'")
    private String pkgUnitYn;

    /*구매관리여부*/
    @Column(name = "pursYn", columnDefinition = "char(1) default 'N'")
    private String pursYn;

    /*자재위처정보관리여부*/
    @Column(name = "matrPosYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String matrPosYn;

    /*가열공정사용여부*/
    @Column(name = "ccp_heat_yn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String ccpHeatYn;


    /*공정사용여부 (대동, 하담, 서울) Y */
    @Column(name = "proc_yn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String procYn;

    /*제품공정내용사용여부---*/
    @Column(name = "prod_cont_yn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String prodContYn;

    /*제품위치정보사용여부*/
    @Column(name = "prod_pos_yn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String prodPosYn;

    /*제품 SET수량관리 여부 : 하담푸드만 'N'*/
    @Column(name = "prod_pkg_yn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String prodPkgYn;

    /*제품 ERP 사용여부 여부 : 대동고려삼만 'Y'로*/
    @Column(name = "erpYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String erpYn;

    /*제품 ERP제품명 사용여부 여부 : 대동고려삼만 'Y'로*/
    @Column(name = "erpProdNmYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String erpProdNmYn;

    /*권한관리 */
    @Column(name="authYn",nullable = false,  columnDefinition = "char(1) default 'N'")
    private String authYn;

    /*SeedKey*/
    @Column(name = "pbszUserKey", nullable = false, columnDefinition = "varchar(30)  default 'daedanmessence2020!@@'")
    private String pbszUserKey;

    /*SeedIv*/
    @Column(name = "pbszIv", nullable = false, columnDefinition = "varchar(20) default 'foreverdaedanmes'")
    private String pbszIv;

    // 자재코드 사용 여부
    @Column(name = "matrCdYn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String matrCdYn;

    // 원산지 사용 여부
    @Column(name = "madeInYn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String madeInYn;

    // 제품코드 사용 여부       ( 유진물산 ) N
    @Column(name = "prodCdYn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String prodCdYn;

    // 제품형태 사용 여부       ( 대동고려삼 ) Y
    @Column(name = "prodShapeYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String prodShapeYn;

    // 관리단위 사용 여부       ( 대동고려삼 ) Y
    @Column(name="mngrUnitYn", nullable = false, columnDefinition = "char(1) default 'N'")
    private String mngrUnitYn;

    /*테블릿AutoSigninID*/
    @Column(name = "autoSignId" ,length = 20)
    private String autoSignId;

    /*메일도메인 */
    @Column(name="mailDomain", length = 50  )
    private String mailDomain;

    /*파일적위기준위치*/
    @Column(name = "fileRoot", length = 250)
    private String fileRoot;

    /*전자태그연동여부(es연구소만 'Y'*/
    @Column(name = "eleTagYn", columnDefinition = "char(1) default 'N'")
    private String eleTagYn;

    /*제품출고단위 : 유진물산-84(Kg) , 기타-81(EA) */
    @Column(name = "outUnit", nullable = false, columnDefinition = "numeric default 81")
    private Long outUnit;


    @Column(name = "usedYn", nullable = false, columnDefinition = "char(1) default 'Y'")
    private String usedYn;
}
