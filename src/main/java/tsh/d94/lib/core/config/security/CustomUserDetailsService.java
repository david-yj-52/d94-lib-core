package tsh.d94.lib.core.config.security;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserRepo;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUserRole.GsUserRoleRepo;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final GsUserRepo userRepo;
	private final GsUserRoleRepo userRoleRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		GsUserModel user = userRepo.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

		return new D94UserDetails(
			user.getEmail(),
			user.getPwdHash(),
			authorities(user.getObjId()),
			user.getObjId(),
			user.getTenant()
		);
	}

	/** 모든 사용자는 ROLE_USER, 그 위에 GS_USER_ROLE 의 역할을 ROLE_{대문자} 로 더한다 */
	private Set<SimpleGrantedAuthority> authorities(String userId) {
		Set<SimpleGrantedAuthority> result = new LinkedHashSet<>();
		result.add(new SimpleGrantedAuthority("ROLE_USER"));
		userRoleRepo.findActiveRoleNames(userId)
			.forEach(role -> result.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
		return result;
	}
}
