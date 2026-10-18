package tsh.d94.lib.core.data.orm.msgServiceConn.gsMsgSrvConn;

import java.util.List;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.constant.UseStatCd;
import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsMsgSrvConnRepo extends BaseJpaRepository<GsMsgSrvConnModel, String> {

	List<GsMsgSrvConnModel> findByTenantAndEnvAndUseStatCd(String tenant, String env, UseStatCd useStatCd);
}
