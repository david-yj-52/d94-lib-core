package tsh.d94.lib.core.apService.auth.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

class AuthRequestValidationTest {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private RegisterRequest register(String email, String name, String password) {
		return RegisterRequest.builder().email(email).name(name).password(password).build();
	}

	@Test
	void validRegister_passes() {
		assertTrue(validator.validate(register("a@b.com", "홍길동", "password1")).isEmpty());
	}

	@Test
	void invalidRegister_rejected() {
		assertFalse(validator.validate(register("not-an-email", "홍길동", "password1")).isEmpty());
		assertFalse(validator.validate(register("a@b.com", " ", "password1")).isEmpty());
		assertFalse(validator.validate(register("a@b.com", "홍길동", "")).isEmpty());
		assertFalse(validator.validate(register("a@b.com", "홍길동", "short1")).isEmpty());
		assertFalse(validator.validate(register("a@b.com", "홍길동", "onlyletters")).isEmpty());
		assertFalse(validator.validate(register("a@b.com", "홍길동", "a1".repeat(36) + "x")).isEmpty()); // 73자
	}

	@Test
	void login_requiresFieldsButNotPasswordRules() {
		assertTrue(validator.validate(LoginRequest.builder().email("a@b.com").password("old").build()).isEmpty());
		assertFalse(validator.validate(LoginRequest.builder().email("").password("x").build()).isEmpty());
		assertFalse(validator.validate(LoginRequest.builder().email("a@b.com").password("").build()).isEmpty());
	}
}
