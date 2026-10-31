package tsh.d94.lib.core.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 로그인 사용자 principal. GS_USER 의 objId·tenant 를 함께 싣는다.
 * HTTP 요청의 tenant 는 토큰이 아니라 이 값(회원가입 때 저장된 GS_USER.TENANT)에서 정한다.
 * TODO 회원 등급(ACL) 정보도 여기에 추가
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class D94UserDetails extends User {

	private final String userId;
	private final String tenant;

	public D94UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
		String userId, String tenant) {
		super(username, password, authorities);
		this.userId = userId;
		this.tenant = tenant;
	}

}
