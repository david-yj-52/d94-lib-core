package tsh.d94.lib.core.config.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
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
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUserRole.GsUserRoleRepo;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private GsUserRepo userRepo;

	@Mock
	private GsUserRoleRepo userRoleRepo;

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
		when(userRoleRepo.findActiveRoleNames("user-1")).thenReturn(List.of());

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


	@Test
	void grants_admin_role_from_gs_user_role() {
		stubUser(List.of("Admin"));

		UserDetails details = service.loadUserByUsername("test@example.com");

		assertThat(details.getAuthorities()).extracting("authority")
			.containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
	}

	@Test
	void user_role_row_does_not_duplicate_role_user() {
		stubUser(List.of("USER"));

		UserDetails details = service.loadUserByUsername("test@example.com");

		assertThat(details.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
	}

	private void stubUser(List<String> roles) {
		GsUserModel user = GsUserModel.builder().objId("user-1").email("test@example.com").pwdHash("hash").tenant("T").build();
		when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(userRoleRepo.findActiveRoleNames("user-1")).thenReturn(roles);
	}
}
