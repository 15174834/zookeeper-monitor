package com.szzc.zookeeper.monitor.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.szzc.zookeeper.monitor.host.ZKHost;
import com.szzc.zookeeper.monitor.host.ZookeeperHosts;

@Configuration
@EnableConfigurationProperties(ZkMonitorProperties.class)
public class ZkMonitorConfiguration {

	@Autowired
	private ZkMonitorProperties zkMonitorProperties;

	@Bean
	public ZookeeperHosts zookeeperHost() {
		String servers = zkMonitorProperties.getServers();
		String[] conns = servers.split(",");
		ZookeeperHosts hosts = new ZookeeperHosts();
		for (String conn : conns) {
			hosts.getHosts().add(new ZKHost(conn.split(":")[0], Integer.valueOf(conn.split(":")[1])));
		}
		return hosts;
	}

	@Bean
	public CuratorFramework zkClient() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework curator = CuratorFrameworkFactory.newClient(zkMonitorProperties.getServers(), retryPolicy);
		curator.start();
		return curator;
	}

}
