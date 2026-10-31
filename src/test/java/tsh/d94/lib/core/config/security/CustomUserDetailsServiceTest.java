package tsh.d94.lib.core.config.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserRepo;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private GsUserRepo userRepo;

	@InjectMocks
	private CustomUserDetailsService service;

	@Test
	void loads_user_with_id_and_tenant() {
		GsUserModel user = GsUserModel.builder()
			.objId("user-1")
			.email("test@example.com")
			.pwdHash("hash")
			.tenant("TENANT_A")
			.build();
		when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		UserDetails details = service.loadUserByUsername("test@example.com");

		assertThat(details).isInstanceOf(D94UserDetails.class);
		D94UserDetails d94User = (D94UserDetails)details;
		assertThat(d94User.getUsername()).isEqualTo("test@example.com");
		assertThat(d94User.getUserId()).isEqualTo("user-1");
		assertThat(d94User.getTenant()).isEqualTo("TENANT_A");
		assertThat(d94User.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
	}

	@Test
	void throws_when_user_not_found() {
		when(userRepo.findByEmail("none@example.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.loadUserByUsername("none@example.com"))
			.isInstanceOf(UsernameNotFoundException.class);
	}

}
