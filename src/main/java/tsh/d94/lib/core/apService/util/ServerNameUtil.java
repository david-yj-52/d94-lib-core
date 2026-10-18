package tsh.d94.lib.core.apService.util;

import java.net.InetAddress;

public class ServerNameUtil {

	public static String getHostName() {

		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
			return "UDF";
		}
	}
}
