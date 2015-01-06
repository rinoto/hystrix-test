import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class HystrixTest {
	@Test
	public void dummyTest() throws Exception {
		String s = new CommandHelloWorld("Bob").execute();
		assertThat(s, is("Hello Bob!"));
	}
}
