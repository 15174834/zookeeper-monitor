package com.szzc.zookeeper.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.util.MapUtils;

import com.szzc.zookeeper.monitor.response.ResponseParser;

@SpringBootApplication
@ConfigurationProperties(prefix = ZkMonitorApplication.PREFIX)
public class ZkMonitorApplication {
	public static final String PREFIX = "";

	private List<String> servers = new ArrayList<String>();

	public List<String> getServers() {
		return servers;
	}

	@Bean
	protected String init() {
		if (MapUtils.isEmpty(ResponseParser.servers)) {
			ResponseParser.servers = new HashMap<String, String>();
			for (String s : servers) {
				String[] info = s.split(",");
				ResponseParser.servers.put(info[0], info[1]);
			}
		}
		return servers.size() + "";
	}

	public static void main(String[] args) {
		SpringApplication.run(ZkMonitorApplication.class, args);
	}
}
