package tsh.d94.lib.core.config.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import tsh.d94.lib.core.apService.auth.JwtService;
import tsh.d94.lib.core.data.orm.common.tenant.TenantContext;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private JwtService jwtService;
	@Mock
	private UserDetailsService userDetailsService;

	@InjectMocks
	private JwtAuthenticationFilter filter;

	@AfterEach
	void tearDown() {
		TenantContext.clear();
		SecurityContextHolder.clearContext();
	}

	private MockHttpServletRequest bearer(String token) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + token);
		return request;
	}

	private D94UserDetails user(String tenant) {
		return new D94UserDetails("test@example.com", "hash",
			List.of(new SimpleGrantedAuthority("ROLE_USER")), "user-1", tenant);
	}

	@Test
	void valid_token_sets_user_tenant_during_chain_and_clears_after() throws Exception {
		D94UserDetails user = user("TENANT_A");
		when(jwtService.extractEmail("tok")).thenReturn("test@example.com");
		when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(user);
		when(jwtService.isTokenValid("tok", user)).thenReturn(true);

		AtomicReference<String> seen = new AtomicReference<>();
		FilterChain chain = (req, res) -> seen.set(TenantContext.get());

		filter.doFilter(bearer("tok"), new MockHttpServletResponse(), chain);

		assertThat(seen.get()).isEqualTo("TENANT_A");
		assertThat(TenantContext.getOrNull()).isNull();
	}

	@Test
	void no_token_does_not_set_tenant() throws Exception {
		AtomicReference<String> seen = new AtomicReference<>("unset");
		FilterChain chain = (req, res) -> seen.set(TenantContext.getOrNull());

		filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), chain);

		assertThat(seen.get()).isNull();
		verifyNoInteractions(jwtService, userDetailsService);
	}

	@Test
	void invalid_token_does_not_set_tenant() throws Exception {
		D94UserDetails user = user("TENANT_A");
		when(jwtService.extractEmail("tok")).thenReturn("test@example.com");
		when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(user);
		when(jwtService.isTokenValid("tok", user)).thenReturn(false);

		AtomicReference<String> seen = new AtomicReference<>("unset");
		FilterChain chain = (req, res) -> seen.set(TenantContext.getOrNull());

		filter.doFilter(bearer("tok"), new MockHttpServletResponse(), chain);

		assertThat(seen.get()).isNull();
	}

	@Test
	void tenant_is_cleared_even_when_chain_throws() {
		D94UserDetails user = user("TENANT_A");
		when(jwtService.extractEmail("tok")).thenReturn("test@example.com");
		when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(user);
		when(jwtService.isTokenValid("tok", user)).thenReturn(true);

		FilterChain chain = (req, res) -> {
			throw new ServletException("boom");
		};

		assertThatThrownBy(() -> filter.doFilter(bearer("tok"), new MockHttpServletResponse(), chain))
			.isInstanceOf(ServletException.class);
		assertThat(TenantContext.getOrNull()).isNull();
	}

}
