package orj.worf.tx.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:worf-tx_jdbc_test.xml"})
public class CommonTest {
	
	@Autowired
	private TestService test;

	@Test
	public void test() throws IOException {
		test.test();
		System.in.read();
	}
}
