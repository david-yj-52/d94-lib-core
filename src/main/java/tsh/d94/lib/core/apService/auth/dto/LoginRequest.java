package tsh.d94.lib.core.apService.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	@NotBlank
	private String email;

	// 비밀번호 규칙은 가입 시에만 — 규칙 이전 가입자도 로그인해야 한다
	@NotBlank
	private String password;
}
