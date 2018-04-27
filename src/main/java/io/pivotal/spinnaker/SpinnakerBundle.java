package io.pivotal.spinnaker;

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;

import com.netflix.hystrix.strategy.HystrixPlugins;

import org.springframework.boot.builder.SpringApplicationBuilder;

import static java.util.Arrays.stream;

public class SpinnakerBundle {
	@SuppressWarnings("unchecked")
	private static final Map<String, Object> ROSCO_PROPS = (Map<String, Object>) (Object)
			com.netflix.spinnaker.rosco.Main.getDEFAULT_PROPS();

	@SuppressWarnings("unchecked")
	private static final Map<String, Object> FRONT50_PROPS = (Map<String, Object>) (Object)
			com.netflix.spinnaker.front50.Main.getDEFAULT_PROPS();

	@SuppressWarnings("unchecked")
	private static final Map<String, Object> GATE_PROPS = (Map<String, Object>) (Object)
			com.netflix.spinnaker.gate.Main.getDEFAULT_PROPS();

	public static void main(String[] args) {
		String config = "./conf/";
		if (args.length > 0)
			config = args[0];

		ROSCO_PROPS.put("spring.config.location", config);
		FRONT50_PROPS.put("spring.config.location", config);
		GATE_PROPS.put("spring.config.location", config);

		new SpringApplicationBuilder()
				.properties(ROSCO_PROPS)
				.sources(com.netflix.spinnaker.rosco.Main.class)
				.run(args);

		// otherwise, HystrixPlugins.getInstance().registerMetricsPublisher(..) will fail
		HystrixPlugins.reset();

		new SpringApplicationBuilder()
				.properties(FRONT50_PROPS)
				.sources(com.netflix.spinnaker.front50.Main.class)
				.run(args);

		HystrixPlugins.reset();

		new SpringApplicationBuilder()
				.properties(GATE_PROPS)
				.sources(com.netflix.spinnaker.gate.Main.class)
				.run(args);
	}

	private static void printClasspath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		Arrays.stream(((URLClassLoader) cl).getURLs())
				.map(url -> stream(url.getFile().split("/"))
						.reduce((acc, segment) -> segment).get())
				.filter(name -> name.endsWith(".jar"))
				.filter(name -> name.matches(".*\\d.*"))
				.sorted()
				.distinct()
				.map(name -> name.substring(0, name.length() - 4))
				.forEach(System.out::println);
	}
}
