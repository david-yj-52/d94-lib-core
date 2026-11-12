package tsh.d94.lib.core.apService.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	@NotBlank
	@Email
	@Size(max = 255)
	private String email;

	@NotBlank
	@Size(min = 2)
	private String name;

	// 72: BCrypt 가 72바이트 이후를 버린다. 멀티바이트 초과분은 BCryptPasswordEncoder 가 거부
	@NotBlank
	@Size(min = 8, max = 72)
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).*$", message = "영문과 숫자를 포함해야 합니다")
	private String password;
}
