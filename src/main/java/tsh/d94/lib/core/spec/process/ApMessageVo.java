package tsh.d94.lib.core.spec.process;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.spec.ApMessageBody;
import tsh.d94.lib.core.spec.ApMessageHead;
import tsh.d94.lib.core.spec.constant.ApSystemList;

@Data
@SuperBuilder
@NoArgsConstructor
public class ApMessageVo<T extends ApMessageBody> {

	ApMessageHead head;
	T body;
	long timestamp;
	private ApSystemList src;
	private ApSystemList tgt;

}
