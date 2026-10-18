package tsh.d94.lib.core.util.message;

import java.util.Optional;

import tsh.d94.lib.core.ApMessage;
import tsh.d94.lib.core.annotation.message.ReplyWith;

public class ReplyResolver {

	public static Optional<Class<? extends ApMessage>> replyTypeOf(Class<? extends ApMessage> requestType) {
		ReplyWith anno = requestType.getAnnotation(ReplyWith.class);
		return Optional.ofNullable(anno).map(ReplyWith::value);
	}

	public static boolean requiresReply(Class<? extends ApMessage> requestType) {
		return requestType.isAnnotationPresent(ReplyWith.class);
	}

	public static void resolverUsage() {
		// instead of using ApMessage.class, use message spec at logic level.
		Class<? extends ApMessage> replyType = ReplyResolver.replyTypeOf(ApMessage.class)
			.orElseThrow(() -> new IllegalStateException("응답 정의 없음"));
	}
}
