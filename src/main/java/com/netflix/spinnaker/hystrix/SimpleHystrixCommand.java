package com.netflix.spinnaker.hystrix;

import java.util.concurrent.Callable;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import groovy.lang.Closure;
import groovy.transform.CompileStatic;
import groovy.util.logging.Slf4j;

/**
 * Modified from kork-hystrix. By excluding kork-hystrix, we avoid an issue where each
 * microservice attempts to register a spectator binding with hystrix (which fails).
 */
@Slf4j
@CompileStatic
public class SimpleHystrixCommand<T> extends HystrixCommand<T> {
	private final Callable<T> work;

	private final Callable<T> fallback;

	public SimpleHystrixCommand(String groupKey,
			String commandKey,
			Closure work) {
		this(groupKey, commandKey, work, null);
	}

	public SimpleHystrixCommand(String groupKey,
			String commandKey,
			Closure work,
			Closure fallback) {
		super(HystrixCommand.Setter.withGroupKey(toGroupKey(groupKey))
				.andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
				.andCommandPropertiesDefaults(createHystrixCommandPropertiesSetter()));
		this.work = () -> {
			//noinspection unchecked
			return (T) work.call();
		};

		this.fallback = fallback != null ? fallback : () -> null;
	}

	@Override
	protected T run() throws Exception {
		return work.call();
	}

	protected T getFallback() {
		T fallbackValue = null;
		try {
			fallbackValue = fallback.call();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (fallbackValue == null) {
			return super.getFallback();
		}

		return fallbackValue;
	}

	static HystrixCommandGroupKey toGroupKey(String name) {
		return HystrixCommandGroupKey.Factory.asKey(name);
	}

	static HystrixCommandProperties.Setter createHystrixCommandPropertiesSetter() {
		return HystrixCommandProperties.defaultSetter();
	}
}
