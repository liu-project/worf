package orj.worf.zkconfig.util;

import java.lang.reflect.Field;

import orj.worf.zkconfig.CfgNode;
import orj.worf.zkconfig.CfgNodeZookeeperClient;

/**
 * CfgNode工具类
 * @author LiuZhenghua
 * 2017年11月20日 下午4:31:09
 */
public class CfgNodeUtil {
	
	/**
	 * 如果zookeeper中存在此节点，则从zookeeper中获取，并设置到obj中
	 * 如果不存在，则将配置上传至zookeeper
	 * @author LiuZhenghua 2017年11月20日 下午4:32:04
	 */
	public static synchronized void saveOrUploadData(CfgNodeZookeeperClient client, Object obj, String path, String desc) throws Exception {
		String propertyName = getPropertyNameFromPath(path);
		Field field = obj.getClass().getDeclaredField(propertyName);
		saveOrUploadData(client, obj, field, path, desc);
	}

	public static synchronized void saveOrUploadData(CfgNodeZookeeperClient client, Object obj, Field field, String path, String desc) throws Exception {
		field.setAccessible(true);
		if (client.exists(path)) {
			//可能客户端在创建完节点后，写入数据之前挂掉了，此处需要重新给节点写入值
			CfgNode cfgNode = client.readData(path);
			if (cfgNode == null || cfgNode.getValue() == null) {
				client.writeData(path, new CfgNode(desc, field.get(obj), obj.getClass().getName()));
			} else {
				Object value = client.readData(path).getValue();
				field.set(obj, value);
			}
		} else {
			client.create(path);
			client.writeData(path, new CfgNode(desc, field.get(obj), obj.getClass().getName()));
		}
	}
	protected static String getPropertyNameFromPath(String path) {
		int index = path.lastIndexOf('/');
		if (index == -1) {
			throw new RuntimeException("path is not a legal cfg node");
		}
		return path.substring(index + 1);
	}
}
