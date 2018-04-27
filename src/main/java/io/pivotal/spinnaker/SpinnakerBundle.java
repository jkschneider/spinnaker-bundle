package io.pivotal.spinnaker;

import java.util.Map;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class SpinnakerBundle {
	@SuppressWarnings("unchecked")
	private static final Map<String, Object> ROSCO_PROPS = (Map<String, Object>) (Object)
			com.netflix.spinnaker.rosco.Main.getDEFAULT_PROPS();

	@SuppressWarnings("unchecked")
	private static final Map<String, Object> FRONT50_PROPS = (Map<String, Object>) (Object)
			com.netflix.spinnaker.front50.Main.getDEFAULT_PROPS();

	public static void main(String[] args) {
		String config = "spinnaker-default.yml";
		if (args.length > 0)
			config = args[0];

		ROSCO_PROPS.put("spring.config.location", config);
		FRONT50_PROPS.put("spring.config.location", config);

		new SpringApplicationBuilder()
				.properties(ROSCO_PROPS).sources(com.netflix.spinnaker.rosco.Main.class)
				.run(args);

		new SpringApplicationBuilder()
				.properties(FRONT50_PROPS).sources(com.netflix.spinnaker.front50.Main.class)
				.run(args);
	}
}