import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;

public class HystrixTest {
	@Test
	public void dummyTest() throws Exception {

		// HystrixServoMetricsPublisher.getInstance().getMetricsPublisherForCommand(commandKey,
		// commandGroupKey, metrics, circuitBreaker, properties)
		// HystrixPlugins.getInstance().registerMetricsPublisher(
		// new HystrixServoMetricsPublisher());

		HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
				.getMetricsPublisher();

		for (int i = 0; i < 10; i++) {
			String s = new CommandHelloWorld("Bob").execute();
			Thread.sleep(100);
			assertThat(s, is("Hello Bob!"));
		}
		Collection<HystrixCommandMetrics> instances = HystrixCommandMetrics
				.getInstances();
		for (HystrixCommandMetrics hystrixCommandMetrics : instances) {
			System.out.println(hystrixCommandMetrics.getTotalTimeMean());
		}
	}

	@Test
	public void testObservable() throws Exception {

		Observable<String> fWorld = new CommandHelloWorld("World").observe();
		Observable<String> fBob = new CommandHelloWorld("Bob").observe();

		// blocking
		assertEquals("Hello World!", fWorld.toBlocking().single());
		assertEquals("Hello Bob!", fBob.toBlocking().single());

		// non-blocking
		// - this is a verbose anonymous inner-class approach and doesn't do
		// assertions
		fWorld.subscribe(new Observer<String>() {

			@Override
			public void onCompleted() {
				// nothing needed here
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(String v) {
				System.out.println("onNext: " + v);
			}

		});

		// non-blocking
		// - also verbose anonymous inner-class
		// - ignore errors and onCompleted signal
		fBob.subscribe(new Action1<String>() {

			@Override
			public void call(String v) {
				System.out.println("onNext: " + v);
			}

		});
	}
}
