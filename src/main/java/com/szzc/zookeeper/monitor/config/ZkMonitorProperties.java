package com.szzc.zookeeper.monitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ZkMonitorProperties.PREFIX)
public class ZkMonitorProperties {
	public static final String PREFIX = "zookeeper";
	private String servers = "127.0.0.1:2181";

	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

}
