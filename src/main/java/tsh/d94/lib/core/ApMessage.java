package tsh.d94.lib.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.spec.ApMessageHead;

@Data
@SuperBuilder
@NoArgsConstructor
public class ApMessage {

	@Schema(description = "메시지 헤디")
	private ApMessageHead head;

}
