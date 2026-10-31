package tsh.d94.lib.core.config.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tsh.d94.lib.core.apService.auth.JwtService;
import tsh.d94.lib.core.data.orm.common.tenant.TenantContext;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// tenant 는 요청 스레드(ThreadLocal)에 걸리므로 스레드 재사용 시 다음 요청으로 새지 않게 항상 비운다
		try {
			authenticate(request, authHeader.substring(7));
			filterChain.doFilter(request, response);
		} finally {
			TenantContext.clear();
		}
	}

	private void authenticate(HttpServletRequest request, String jwt) {
		final String userEmail;
		try {
			userEmail = jwtService.extractEmail(jwt);
			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
				if (jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities()
					);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					if (userDetails instanceof D94UserDetails d94User) {
						TenantContext.set(d94User.getTenant());
					}
				}
			}
		} catch (Exception e) {
			// Token validation failed or other issue - let it proceed to next filter
			// SecurityConfig will handle the unauthorized access if required
		}
	}
}
