/*package orj.worf.nosql.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.internal.ResultMap;

import orj.worf.mongodb.service.TestRemoteService;
import orj.worf.mongodb.vo.Address;
import orj.worf.mongodb.vo.TestMongodb;

public class TestMongoDBService {

	@Test
	public void test() {

		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
				"worf-mongodb_config.xml");
		classPathXmlApplicationContext.start();
		TestRemoteService testRemoteService = (TestRemoteService) classPathXmlApplicationContext
				.getBean("testRemoteService");

		try {
			TestMongodb mongodb = new TestMongodb();
			List<Address> addList = new ArrayList<Address>();
			Address address = new Address();

			address.setName("皓皓");
			address.setIphone("18123456789");
			address.setDefAdd(0);
			address.setAddress("深圳市福田区红松大厦");
			addList.add(address);
			mongodb.setLid(103456987L);
			mongodb.setName("皓皓123");
			mongodb.setList(addList);
			ResultMap map = testRemoteService.saveTestMongodb(mongodb);
			System.out.println("result ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFindMongodb() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
				"worf-mongodb_config.xml");
		classPathXmlApplicationContext.start();
		TestRemoteService testRemoteService = (TestRemoteService) classPathXmlApplicationContext
				.getBean("testRemoteService");
		TestMongodb mongodb = testRemoteService.findTestMongodbById(103456987L);
		if (mongodb != null) {
			System.out.println(mongodb.getName());
			System.out.println(mongodb.getLid());
			System.out.println(mongodb.getList().get(0).getIphone());
		}
	}

	@Test
	public void testUpdateMongodb() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
				"worf-mongodb_config.xml");
		classPathXmlApplicationContext.start();
		TestRemoteService testRemoteService = (TestRemoteService) classPathXmlApplicationContext
				.getBean("testRemoteService");
		TestMongodb mongodb = new TestMongodb();
		List<Address> addList = new ArrayList<Address>();
		Address address = new Address();
		address.setName("Miss姐");
		address.setIphone("18188889922");
		address.setDefAdd(0);
		address.setAddress("深圳市福田区8899");
		addList.add(address);
		mongodb.setLid(103456987L);
		mongodb.setName("王者66");
		mongodb.setList(addList);
		testRemoteService.updateMongodb(mongodb);
	}
}
*/