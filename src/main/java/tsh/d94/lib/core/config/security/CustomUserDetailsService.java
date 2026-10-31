package tsh.d94.lib.core.config.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserRepo;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final GsUserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		GsUserModel user = userRepo.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

		return new D94UserDetails(
			user.getEmail(),
			user.getPwdHash(),
			List.of(new SimpleGrantedAuthority("ROLE_USER")), // TODO Implement actual roles from GS_ROLE
			user.getObjId(),
			user.getTenant()
		);
	}
}
