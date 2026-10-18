package tsh.d94.lib.core.spec.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.ApMessage;
import tsh.d94.lib.core.spec.ApMessageBody;
import tsh.d94.lib.core.spec.common.ResultVo;
import tsh.d94.lib.core.spec.constant.ApMessageList;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HealthCheckRep extends ApMessage {

	public static final ApMessageList eventNm = ApMessageList.HealthCheckRep;

	@Valid
	@NotNull(message = "body는 필수값 입니다.")
	private HealthCheckRep.Body body;

	@Data
	@SuperBuilder
	@NoArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	public static class Body extends ApMessageBody {

		@NotNull(message = "필수값 누락")
		String reqTraceId;

		@NotNull(message = "필수값 누락")
		String reqSrvNm;

		@NotNull(message = "필수값 누락")
		private String serviceNm;

		@NotNull(message = "필수값 누락")
		private ResultVo result;

	}

}
