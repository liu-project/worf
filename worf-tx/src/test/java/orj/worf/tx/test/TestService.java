package orj.worf.tx.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class TestService {
	
	@Autowired
	private Test2Service test2Service;
	
	@Transactional
	public void test() {
		System.out.println("hello world!");
		test2Service.test2_1();
		test2Service.test2_2();
	}

}
