package tsh.d94.lib.core.data.orm.common.tenant;

import tsh.d94.lib.core.data.orm.common.exception.TenantMissingException;

public final class TenantContext {

	private static final ThreadLocal<String> TENANT_HOLDER = new ThreadLocal<>();

	private TenantContext() {
		throw new UnsupportedOperationException("TenantContext is a utility class");
	}

	public static void set(String tenant) {
		TENANT_HOLDER.set(tenant);
	}

	public static String get() {
		String tenant = TENANT_HOLDER.get();
		if (tenant == null || tenant.isBlank()) {
			throw new TenantMissingException();
		}
		return tenant;
	}

	/** 설정되지 않았으면 null. 이전 값 보존·비동기 전파처럼 비어 있어도 되는 곳에서만 쓴다. */
	public static String getOrNull() {
		return TENANT_HOLDER.get();
	}

	public static void clear() {
		TENANT_HOLDER.remove();
	}

}
