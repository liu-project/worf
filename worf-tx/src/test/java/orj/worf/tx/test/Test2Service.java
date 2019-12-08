package orj.worf.tx.test;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class Test2Service {
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void test2_1() {
		System.out.println("hello world2_1!");
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void test2_2() {
		System.out.println("hello world2_2!");
	}

}
