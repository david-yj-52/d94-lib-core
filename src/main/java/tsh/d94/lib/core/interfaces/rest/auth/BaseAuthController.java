package tsh.d94.lib.core.interfaces.rest.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tsh.d94.lib.core.apService.auth.AuthService;
import tsh.d94.lib.core.apService.auth.dto.LoginRequest;
import tsh.d94.lib.core.apService.auth.dto.LoginResponse;
import tsh.d94.lib.core.apService.auth.dto.RefreshRequest;
import tsh.d94.lib.core.apService.auth.dto.RegisterRequest;
import tsh.d94.lib.core.interfaces.rest.ApiResponse;

@RequiredArgsConstructor
public abstract class BaseAuthController {

	protected final AuthService authService;

	@Value("${application.security.cookie.secure:true}")
	protected boolean cookieSecure;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
		preRegister(request);
		authService.register(request);
		postRegister(request);
		return ApiResponse.noContent();
	}

	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(
		@Valid @RequestBody LoginRequest request,
		HttpServletResponse response
	) {
		preLogin(request);
		LoginResponse loginResponse = authService.login(request);
		setRefreshTokenCookie(response, loginResponse.getRefreshToken());

		// Remove refreshToken from body as it's in cookie
		loginResponse.setRefreshToken(null);

		postLogin(request, loginResponse);
		return ApiResponse.ok(loginResponse);
	}

	/**
	 * refreshToken 은 쿠키(브라우저 직접 호출) 또는 본문 {@code {refreshToken}}(서버 간 호출)으로 받는다.
	 * UMD 는 요청의 Cookie 헤더를 버리므로 d94-web 서버는 본문으로 보낸다.
	 */
	@PostMapping("/refresh")
	public ApiResponse<LoginResponse> refresh(
		HttpServletRequest request,
		HttpServletResponse response,
		@RequestBody(required = false) RefreshRequest body
	) {
		String refreshToken = resolveRefreshToken(request, body);
		LoginResponse loginResponse = authService.refresh(refreshToken);
		setRefreshTokenCookie(response, loginResponse.getRefreshToken());

		loginResponse.setRefreshToken(null);

		return ApiResponse.ok(loginResponse);
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(
		HttpServletRequest request,
		HttpServletResponse response,
		@RequestBody(required = false) RefreshRequest body
	) {
		String refreshToken = resolveRefreshToken(request, body);
		authService.logout(refreshToken);
		clearRefreshTokenCookie(response);
		return ApiResponse.noContent();
	}

	protected void preRegister(RegisterRequest request) {
	}

	protected void postRegister(RegisterRequest request) {
	}

	protected void preLogin(LoginRequest request) {
	}

	protected void postLogin(LoginRequest request, LoginResponse response) {
	}

	protected void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(cookieSecure);
		cookie.setPath("/");
		cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
		response.addCookie(cookie);
	}

	protected void clearRefreshTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(cookieSecure);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/** 쿠키 우선, 없으면 본문. 둘 다 없으면 IllegalArgumentException */
	protected String resolveRefreshToken(HttpServletRequest request, RefreshRequest body) {
		String fromCookie = getRefreshTokenFromCookie(request);
		if (fromCookie != null) {
			return fromCookie;
		}
		if (body != null && body.getRefreshToken() != null && !body.getRefreshToken().isBlank()) {
			return body.getRefreshToken();
		}
		throw new IllegalArgumentException("Refresh token not found");
	}

	/** refreshToken 쿠키 값. 없으면 null */
	protected String getRefreshTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
