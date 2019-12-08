package orj.worf.zk_config.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import orj.worf.zkconfig.CfgNode;
import orj.worf.zkconfig.CfgNodeZookeeperClient;
import orj.worf.zkconfig.ZookeeperClientFactory;
import orj.worf.zkconfig.zkclient.ZkClientZookeeperClientFactory;

public class CfgNodeZkClientTest {
	
	CfgNodeZookeeperClient zkClient = null;
	@Before
	public void init() {
		ZookeeperClientFactory factory = new ZkClientZookeeperClientFactory();
		zkClient = factory.getInstanceForConfigUse("rjs-checking", "139.196.94.189:2181,139.196.94.189:2182,139.196.94.189:2183");
	}
	
	@Test
	public void test() {
		String parentPath = "/";
		List<String> children = zkClient.getChildren(parentPath);
		for (String child : children) {
			System.out.println("key:" + child + ", value:" + zkClient.readData(parentPath, child));
		}
		
		String moneyLogKey = "/secretKey/moneyLogKey";
		if (!zkClient.exists(moneyLogKey)) {
			zkClient.create(moneyLogKey);
		}
		zkClient.writeData(moneyLogKey, new CfgNode("资金日志签名Key", "asdlfkj@dj!.sdfX"));
		System.out.println(zkClient.readData(moneyLogKey));
	}

}
