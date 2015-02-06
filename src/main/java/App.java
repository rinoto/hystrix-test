import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

public class App {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8090);
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.NO_SESSIONS);
		server.setHandler(context);
		final HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();
		final ServletHolder holder = new ServletHolder(servlet);
		context.addServlet(holder, "/hystrix.stream");
		server.start();
	}

}
