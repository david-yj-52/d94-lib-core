package tsh.d94.lib.core.data.orm.msgServiceConn.gsMsgSrvConn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tsh.d94.lib.core.data.constant.UseStatCd;
import tsh.d94.lib.core.data.orm.common.access.AbstractCrudService;
import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Slf4j
@Service
public class GsMsgSrvConnAccess extends AbstractCrudService<GsMsgSrvConnModel, String> {

	@Autowired
	GsMsgSrvConnRepo repo;

	@Override
	protected BaseJpaRepository<GsMsgSrvConnModel, String> getRepository() {
		return repo;
	}

	public List<GsMsgSrvConnModel> findByTenantAndEnv(String tenant, String env) {
		return this.repo.findByTenantAndEnvAndUseStatCd(tenant, env, UseStatCd.Usable);
	}

}
