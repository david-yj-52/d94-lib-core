package tsh.d94.lib.core.lifecycle;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tsh.d94.lib.core.exception.ApplicationException;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class RequestLifeCycleVo {

	@Valid
	@NotNull(message = "tenantId is mandatory")
	String tenantId;
	// elapsed time management

	// request result management

	// extra data management

	// interface management

	// processing status

	// exception management
	ApplicationException applicationException;

}
