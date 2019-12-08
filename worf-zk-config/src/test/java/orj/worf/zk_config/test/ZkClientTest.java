package orj.worf.zk_config.test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import orj.worf.zkconfig.CfgNode;
import orj.worf.zkconfig.ZookeeperClient;
import orj.worf.zkconfig.ZookeeperClientFactory;
import orj.worf.zkconfig.ZookeeperDataListener;
import orj.worf.zkconfig.zkclient.ZkClientZookeeperClientFactory;

public class ZkClientTest {
	
	private ZookeeperClient zkClient = null;
	
	@Before
	public void init() {
		ZookeeperClientFactory factory = new ZkClientZookeeperClientFactory();
		zkClient = factory.getInstance("139.196.94.189:2181,139.196.94.189:2182,139.196.94.189:2183");
	}

	@Test
	public void test() throws IOException {
		CfgNode zookeeperNode = new CfgNode("对账第一笔资金日志Id", "159872");
		CfgNode zookeeperNode2 = new CfgNode("对账开关", "1");
		
		if (!zkClient.exists("/config/rjs-checking/serverConfig/firstMoneyLogId")) {
			zkClient.create("/config/rjs-checking/serverConfig/firstMoneyLogId");
		}
		
		if (!zkClient.exists("/config/rjs-checking/serverConfig/check")) {
			zkClient.create("/config/rjs-checking/serverConfig/check");
		}
	
		long timeStart = new Date().getTime();
		for (int i = 0; i < 5; i++) {
			zkClient.writeData("/config/rjs-checking/serverConfig/firstMoneyLogId", zookeeperNode);
			zkClient.writeData("/config/rjs-checking/serverConfig/check", zookeeperNode2);
		}
		System.out.println("time used:" + (new Date().getTime() - timeStart));
		
		CfgNode readNode = (CfgNode)zkClient.readData("/config/rjs-checking/serverConfig/firstMoneyLogId");
		CfgNode readNode2 = (CfgNode)zkClient.readData("/config/rjs-checking/serverConfig/check");
		System.out.println(readNode);
		System.out.println(readNode2);
		
		String parentPath = "/config/rjs-checking/serverConfig/";
		List<String> children = zkClient.getChildren(parentPath);
		for (String child : children) {
			System.out.println(zkClient.readData(parentPath + child));
		}
		
//		zookeeperClient.delete("/config/rjs-checking", true);
		
		/*zkClient.subscribeDataChanges(parentPath + "check", new ZookeeperDataListener() {
			
			public void handleDataDeleted(String s) throws Exception {
				
			}
			
			public void handleDataChange(String s, Object obj) throws Exception {
				System.out.println(s);
				System.out.println(obj);
			}
		});*/
		
		zkClient.subscribeChildrenDataChanges(parentPath, new ZookeeperDataListener() {
			
			public void handleDataDeleted(String s) throws Exception {
			}
			
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println("节点：" + path + "内容更新为：" + data);
			}
		});
		System.in.read();
	}
	
	@Test
	public void testChangeData() {
		zkClient.writeData("/config/rjs-checking/serverConfig/check", new CfgNode("对账开关", "0"));
	}
}
