package tsh.d94.lib.core.data.orm.messageReply.gsSolMsgRep;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.constant.GlobalTableName;
import tsh.d94.lib.core.data.constant.MsgRepStatCd;
import tsh.d94.lib.core.data.orm.common.model.BaseModel;
import tsh.d94.lib.core.spec.constant.ApMessageList;

@Entity
@Table(
	name = GlobalTableName.GS_SOL_MSG_REP,
	uniqueConstraints = {
		@UniqueConstraint(name = GsSolMsgRepModel.UK01, columnNames = {"reqSrvNm", "reqTraceId"})
	}
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class GsSolMsgRepModel extends BaseModel {

	public static final String UK01 = "uk_sol_msg_rep_01";

	@Column(name = "REQ_SRV_NM")
	private String reqSrvNm;

	@Column(name = "REQ_TRACE_ID")
	private String reqTraceId;

	@Column(name = "RECV_EVNT_NM")
	private ApMessageList recvEvntNm;

	@Column(name = "RECV_TOPIC_NM")
	private String recvTopicNm;

	@Column(name = "SELECTOR_KEY")
	private String selectorKey;

	@Column(name = "REP_STAT_CD")
	@Enumerated(EnumType.STRING)
	private MsgRepStatCd repStatCd;

}
