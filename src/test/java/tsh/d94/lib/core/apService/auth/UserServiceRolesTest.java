package tsh.d94.lib.core.apService.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserRepo;

@ExtendWith(MockitoExtension.class)
class UserServiceRolesTest {

	@Mock
	private GsUserRepo userRepo;

	@InjectMocks
	private UserService service;

	@AfterEach
	void clear() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void profile_carries_roles_without_prefix() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
			"a@b.c", null, List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"))));
		when(userRepo.findByEmail("a@b.c")).thenReturn(Optional.of(
			GsUserModel.builder().email("a@b.c").userNm("A").build()));

		assertThat(service.getCurrentUserProfile().getRoles()).containsExactly("USER", "ADMIN");
	}
}
