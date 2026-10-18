package tsh.d94.lib.core.exception;

import tsh.d94.lib.core.lifecycle.RequestLifeCycleVo;

public class ApplicationException extends RuntimeException {

	private final RequestLifeCycleVo requestLifeCycleVo;

	public ApplicationException(String message, RequestLifeCycleVo requestLifeCycleVo) {
		super(message);
		this.requestLifeCycleVo = requestLifeCycleVo;
	}
}
