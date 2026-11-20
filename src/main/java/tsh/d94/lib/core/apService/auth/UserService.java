package tsh.d94.lib.core.apService.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import tsh.d94.lib.core.apService.auth.dto.UpdateProfileRequest;
import tsh.d94.lib.core.apService.auth.dto.UserResponse;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;
import tsh.d94.lib.core.data.orm.usersAndPermissions.gsUser.GsUserRepo;

@Service
@RequiredArgsConstructor
public class UserService {

	private final GsUserRepo userRepo;

	public UserResponse getCurrentUserProfile() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userRepo.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		return UserResponse.builder()
			.email(user.getEmail())
			.name(user.getUserNm())
			.avatarUrl(user.getAvatarUrl())
			.roles(currentRoles())
			.build();
	}

	@Transactional
	public UserResponse updateProfile(UpdateProfileRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userRepo.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		if (request.getName() != null) {
			user.setUserNm(request.getName());
		}
		if (request.getAvatarUrl() != null) {
			user.setAvatarUrl(request.getAvatarUrl());
		}

		userRepo.save(user);

		return UserResponse.builder()
			.email(user.getEmail())
			.name(user.getUserNm())
			.avatarUrl(user.getAvatarUrl())
			.roles(currentRoles())
			.build();
	}

	private static List<String> currentRoles() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
			.distinct()
			.toList();
	}
}
