package tsh.d94.lib.core.messaging.solace.vo;

import java.util.Map;

import com.solacesystems.jcsmp.DeliveryMode;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.spec.constant.ApMessageList;

@SuperBuilder
@Data
@NoArgsConstructor
public class SolaceOutBoundMessage {
	Map<String, Object> msgHeader;
	ApMessageList eventNm;
	private String destination;      // Queue명 또는 Topic명
	private String payload;          // 메시지 내용 (JSON String)
	private DeliveryMode deliveryMode; // DIRECT or PERSISTENT
	private Integer timeToLive;      // TTL (ms), null이면 무제한
	private String correlationId;    // 요청 추적용 ID
}
