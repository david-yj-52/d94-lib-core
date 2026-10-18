package tsh.d94.lib.core.data.orm.common.tenant;

public interface TenantResolver {

	String getTenant();

	void setTenant(String tenant);

	void clear();

}
