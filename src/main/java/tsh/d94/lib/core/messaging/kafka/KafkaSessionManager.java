package tsh.d94.lib.core.messaging.kafka;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import tsh.d94.lib.core.config.ApplicationProperties;
import tsh.d94.lib.core.data.orm.msgServiceConn.gsMsgSrvConn.GsMsgSrvConnModel;
import tsh.d94.lib.core.messaging.AbstractMessageSessionManager;

@Slf4j
public class KafkaSessionManager extends AbstractMessageSessionManager {

	public KafkaSessionManager(List<GsMsgSrvConnModel> infos) {

		log.info("groupId: {}, service:{}, version:{}", ApplicationProperties.getApplicationModuleName(),
			ApplicationProperties.getApplicationServiceName(), ApplicationProperties.getApplicationVersion());

	}

	@Override
	protected void stopSession() {

	}

	@Override
	protected void startSession() {

	}

	@Override
	protected void checkSession() {

	}
}
