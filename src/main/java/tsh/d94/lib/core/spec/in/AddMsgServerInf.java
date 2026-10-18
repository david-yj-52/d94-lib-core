package tsh.d94.lib.core.spec.in;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.ApMessage;
import tsh.d94.lib.core.data.constant.MessagingSolutionType;
import tsh.d94.lib.core.data.constant.UseYn;
import tsh.d94.lib.core.spec.ApMessageBody;
import tsh.d94.lib.core.spec.constant.ApMessageList;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AddMsgServerInf extends ApMessage {

	public static final ApMessageList eventNm = ApMessageList.AddMsgServerInfo;

	@Valid
	@NotNull(message = "body는 필수값입니다.")
	private AddMsgServerInf.Body body;

	@Data
	@SuperBuilder
	@NoArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	public static class Body extends ApMessageBody {

		@NotNull(message = "필수값 누락")
		@JsonInclude(JsonInclude.Include.ALWAYS)
		private MessagingSolutionType solNm;

		@NotNull(message = "필수값 누락")
		private String conUserId;

		@NotNull(message = "필수값 누락")
		private String pwd;

		@NotNull(message = "필수값 누락")
		private String domain;

		@NotNull(message = "필수값 누락")
		private String env;

		@NotNull(message = "필수값 누락")
		private String host;

		@NotNull(message = "필수값 누락")
		private int port;

		@NotNull(message = "필수값 누락")
		private UseYn defaultYn;
	}

}
