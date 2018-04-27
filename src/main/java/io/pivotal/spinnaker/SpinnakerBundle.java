package io.pivotal.spinnaker;

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.builder.SpringApplicationBuilder;

import static java.util.Arrays.stream;

public class SpinnakerBundle {
	// @SuppressWarnings("unchecked")
	// private static final Map<String, Object> ROSCO_PROPS = (Map<String, Object>) (Object)
	// 		com.netflix.spinnaker.rosco.Main.getDEFAULT_PROPS();

	@SuppressWarnings("unchecked")
	private static final Map<String, Object> FRONT50_PROPS = (Map<String, Object>) (Object)
			com.netflix.spinnaker.front50.Main.getDEFAULT_PROPS();

	public static void main(String[] args) throws ClassNotFoundException {
//		String config = "/Users/jschneider/Projects/github/jkschneider/spinnaker-bundle/spinnaker-bundle";
//		if (args.length > 0)
//			config = args[0];

//		ROSCO_PROPS.put("spring.config.location", config);
//		FRONT50_PROPS.put("spring.config.location", config);

		// new SpringApplicationBuilder()
		// 		.properties(ROSCO_PROPS)
		// 		.sources(com.netflix.spinnaker.rosco.Main.class)
		// 		.run(args);

		new SpringApplicationBuilder()
				.properties(FRONT50_PROPS)
				.sources(com.netflix.spinnaker.front50.Main.class)
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
