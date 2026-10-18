package tsh.d94.lib.core.spec;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.spec.constant.ApMessageList;
import tsh.d94.lib.core.spec.constant.ApSystemList;

@Data
@SuperBuilder
@NoArgsConstructor
public class ApMessageHead {

	private ApSystemList src;
	private ApSystemList tgt;
	private String traceId;
	private ApMessageList eventNm;

}
