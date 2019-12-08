package orj.worf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import net.sf.json.util.JSONUtils;

public class Test {
	
	

	final static String DEATH_STRING = "{\"a\":\"\\x";
	
	private String s2 = "hello";

	@SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
		URL url = ClassLoader.getSystemResource("idcarddb/area.json");
		File file = new File(url.getFile());
		byte[] b = new byte[(int)file.length()];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		is.close();
		String json = new String(b);
		Map<String, Object> map = JsonUtils.parseJSON(json, Map.class);
		ArrayList<String> list = new ArrayList<String>();
		String head = "insert into t_idcard_area(id, code, name, parent_id, level) values";
		for(String key : map.keySet()) {
			Map<String, String> valueMap = (Map<String, String>)map.get(key);
			list.add(String.format("(%d, '%s', '%s', '%s', %d)", Integer.valueOf(key) ,valueMap.get("code") ,valueMap.get("text"), valueMap.get("code").substring(0, 4), 3));
		}
		String str = head + StringUtils.join(list, ",") + ";";
		System.out.println(str);
		
		Test1.test();
		//URL url2 = ClassLoader.getSystemResource("idcarddb/test.sql");
		/*File out = new File("src/main/resources/idcarddb/test.sql");
		FileOutputStream os = new FileOutputStream(out, true);
		os.write(str.getBytes());
		os.flush();
		os.close();*/
		
		String s = "a" + "b" + "c" + "d";
		String s1 = "a";
		String s2 = s1 + "b";
		String s3 = s1 + "b" + "c";
		String s4 = s1 + "bc";
		System.out.println(s1 == s2);
		System.out.println(s == "abcd");
		System.out.println(new Inner().test());
    }
	
	static class Inner {
		int x = 1;
		int test(){
			try {
				return x;
			} finally {
				++x;
			}
			
		}
	}
}

abstract class Test1 {
	static void test() {
		System.out.println("hello world!");
	}
}