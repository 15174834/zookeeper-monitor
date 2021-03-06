package me.douboo.zookeeper.monitor.host;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import me.douboo.zookeeper.monitor.response.MonitorResponse;
import me.douboo.zookeeper.monitor.response.ResponseParser;

public class ZKHost {
	private String host;
	private int port;
	private MonitorResponse stat;

	public MonitorResponse getStat() {
		return stat;
	}

	public void setStat(MonitorResponse stat) {
		this.stat = stat;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ZKHost(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public MonitorResponse command(ZookeeperCommand command) {
		ResponseParser parser = ResponseParser.parser(command);
		return parser.parse(command(host, port, command));
	}

	private static String command(String host, int port, ZookeeperCommand command) {
		Socket socket = null;
		try {
			socket = new Socket();
			socket.setSoLinger(false, 10);
			socket.setSoTimeout(20000);
			socket.connect(new InetSocketAddress(host, port));
			IOUtils.write(command.name(), socket.getOutputStream(), Charset.defaultCharset());
			return IOUtils.toString(socket.getInputStream(), Charset.defaultCharset());
		} catch (Exception e) {
			return "Zookeeper instance not found.";
		} finally {
			IOUtils.closeQuietly(socket);
		}
	}
}