//package edu.hm.dako.chat.server.datasink.config;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class DBConfig {
//
//	public static Map<String, Object> getPersistConfig(Database database, Integer port) {
//		Map<String, String> env = System.getenv();
//		Map<String, Object> configOverrides = new HashMap<String, Object>();
//
//		for (String envName : env.keySet()) {
//			if (envName.contains("DB_HOSTURL")) {
//				configOverrides.put("javax.persistence.jdbc.url",
//						"jdbc:mysql://" + env.get(envName) + ":" + port + "/" + database);
//			}
//			if (envName.contains("DB_USER")) {
//				configOverrides.put("javax.persistence.jdbc.user", env.get(envName));
//			}
//			if (envName.contains("DB_PWD")) {
//				configOverrides.put("javax.persistence.jdbc.password", env.get(envName));
//			}
//		}
//
//		return configOverrides;
//	}
//	
//	
//}