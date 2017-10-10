package me.doubbo.zookeeper.monitor.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import me.doubbo.zookeeper.monitor.host.ZKHost;
import me.doubbo.zookeeper.monitor.host.ZookeeperCommand;
import me.doubbo.zookeeper.monitor.host.ZookeeperHosts;

@Controller
@RequestMapping
public class MonitorContoller {

	@Autowired
	private ZookeeperHosts zookeeperHosts;

	@Autowired
	private CuratorFramework zkClient;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView summary() {
		ModelAndView indexView = new ModelAndView("index");
		try {
			for (ZKHost host : zookeeperHosts.getHosts()) {
				host.setStat(host.command(ZookeeperCommand.stat));
			}
			indexView.addObject("hosts", zookeeperHosts.getHosts());
			indexView.addObject(ZookeeperCommand.nods.name(), zkClient.getChildren().forPath("/"));
			indexView.addObject("path", "");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return indexView;
	}

	@RequestMapping(path = "/path/**", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> path(HttpServletRequest request) {
		String path = request.getServletPath();
		path = path.replace("/path", "");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("code", 0);
			result.put("path", path);
			result.put(ZookeeperCommand.nods.name(), zkClient.getChildren().forPath(path));
		} catch (Exception e) {
			result.put("code", 100);
		}
		return result;
	}

	@RequestMapping(path = "/data/**", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> data(HttpServletRequest request) {
		String path = request.getServletPath();
		path = path.replace("/data", "");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("code", 0);
			result.put("path", path);
			result.put(ZookeeperCommand.data.name(), new String(zkClient.getData().forPath(path)));
		} catch (Exception e) {
			result.put("code", 100);
		}
		return result;
	}
}
