package tsh.d94.lib.core.apService.auth;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tsh.d94.lib.core.apService.auth.dto.LoginRequest;
import tsh.d94.lib.core.apService.auth.dto.LoginResponse;
import tsh.d94.lib.core.apService.auth.dto.RegisterRequest;
import tsh.d94.lib.core.config.ApplicationProperties;
import tsh.d94.lib.core.data.constant.UseStatCd;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserRepo;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final GsUserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;

	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	@Transactional
	public void register(RegisterRequest request) {
		String email = normalizeEmail(request.getEmail());
		if (userRepo.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}

		GsUserModel user = GsUserModel.builder()
			.email(email)
			.userNm(request.getName())
			.pwdHash(passwordEncoder.encode(request.getPassword()))
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INTERNAL-REG") // Placeholder
			.useStatCd(UseStatCd.Usable)
			.evtNm("Register")
			.prevEvntNm("None")
			.build();

		try {
			userRepo.saveAndFlush(user);
		} catch (DataIntegrityViolationException e) {
			// 동시 가입: 위 중복 체크를 둘 다 통과하고 EMAIL 유니크 제약(UK01)에서 걸린 경우
			throw new IllegalArgumentException("Email already exists");
		}
	}

	public LoginResponse login(LoginRequest request) {
		String email = normalizeEmail(request.getEmail());
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(email, request.getPassword())
		);

		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		GsUserModel user = userRepo.findByEmail(email).orElseThrow();

		String accessToken = jwtService.generateAccessToken(userDetails);
		String refreshToken = jwtService.generateRefreshToken();

		refreshTokenService.saveRefreshToken(refreshToken, user.getObjId());

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.tokenType("Bearer")
			.expiresIn(jwtExpiration / 1000)
			.build();
	}

	public LoginResponse refresh(String refreshToken) {
		String userId = refreshTokenService.getUserIdByToken(refreshToken)
			.orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

		GsUserModel user = userRepo.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// Rotation: Delete old, create new
		refreshTokenService.deleteRefreshToken(refreshToken);
		String newRefreshToken = jwtService.generateRefreshToken();
		refreshTokenService.saveRefreshToken(newRefreshToken, user.getObjId());

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		String accessToken = jwtService.generateAccessToken(userDetails);

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(newRefreshToken)
			.tokenType("Bearer")
			.expiresIn(jwtExpiration / 1000)
			.build();
	}

	public void logout(String refreshToken) {
		refreshTokenService.deleteRefreshToken(refreshToken);
	}

	/** 저장·조회 모두 이 형태로만 — A@x.com 과 a@x.com 은 같은 계정 */
	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
