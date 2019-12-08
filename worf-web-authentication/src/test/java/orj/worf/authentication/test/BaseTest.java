package orj.worf.authentication.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import orj.worf.authentication.configure.WebConfigure;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:worf-web-authentication_test_config.xml"})
public class BaseTest {

	@Autowired
	private WebConfigure webConfigure;
	
	@Test
	public void test() throws IOException {
		System.out.println(webConfigure.getAuthProjectName());
		System.out.println(WebConfigure.getInstance());
		System.in.read();
	}
}
