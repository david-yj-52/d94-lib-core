package tsh.d94.lib.core.config.security;

import org.springframework.security.core.userdetails.User;
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

		return User.builder()
			.username(user.getEmail())
			.password(user.getPwdHash())
			.roles("USER") // TODO Implement actual roles from GS_ROLE
			.build();
	}
}
