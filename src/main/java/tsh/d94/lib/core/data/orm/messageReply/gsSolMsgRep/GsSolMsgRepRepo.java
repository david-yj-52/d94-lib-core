package tsh.d94.lib.core.data.orm.messageReply.gsSolMsgRep;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.constant.UseStatCd;
import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsSolMsgRepRepo extends BaseJpaRepository<GsSolMsgRepModel, String> {

	GsSolMsgRepModel findByTenantAndTraceIdAndUseStatCd(String tenant, String traceId, UseStatCd useStatCd);

}
