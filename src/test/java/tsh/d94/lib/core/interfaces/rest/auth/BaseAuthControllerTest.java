package tsh.d94.lib.core.interfaces.rest.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.Cookie;
import tsh.d94.lib.core.apService.auth.dto.RefreshRequest;

class BaseAuthControllerTest {

	private final BaseAuthController controller = new BaseAuthController(null) {
	};

	@Test
	void refreshToken_은_쿠키_우선_없으면_본문() {
		MockHttpServletRequest withCookie = new MockHttpServletRequest();
		withCookie.setCookies(new Cookie("refreshToken", "from-cookie"));
		assertEquals("from-cookie", controller.resolveRefreshToken(withCookie, new RefreshRequest("from-body")));

		// UMD 는 Cookie 헤더를 버린다 → d94-web 서버는 본문으로 보낸다
		assertEquals("from-body", controller.resolveRefreshToken(new MockHttpServletRequest(), new RefreshRequest("from-body")));
	}

	@Test
	void 둘_다_없거나_빈_값이면_거절() {
		MockHttpServletRequest empty = new MockHttpServletRequest();
		assertThrows(IllegalArgumentException.class, () -> controller.resolveRefreshToken(empty, null));
		assertThrows(IllegalArgumentException.class, () -> controller.resolveRefreshToken(empty, new RefreshRequest(" ")));
	}
}
