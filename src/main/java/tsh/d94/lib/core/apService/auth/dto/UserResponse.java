package tsh.d94.lib.core.apService.auth.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private String email;
	private String name;
	private String avatarUrl;
	/** 역할명 (ROLE_ 접두사 없음, 예: USER, ADMIN) */
	private List<String> roles;
}
