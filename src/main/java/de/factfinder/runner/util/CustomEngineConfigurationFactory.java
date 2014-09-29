package de.factfinder.runner.util;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.EngineConfigurationFactory;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.BasicClientConfig;
import org.apache.axis.configuration.BasicServerConfig;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.axis.transport.http.HTTPSender;

/**
 * Overrides the default client configuration to use {@link CommonsHTTPSender} instead of {@link HTTPSender}.
 */
public class CustomEngineConfigurationFactory implements EngineConfigurationFactory {

	/**
	 * Return our implementation with the default settings except using {@link CommonsHTTPSender} instead of {@link HTTPSender}.
	 * 
	 * @param param factory parameter.
	 * @return our own factory.
	 */
	public static EngineConfigurationFactory newFactory(final Object param) {
		return new CustomEngineConfigurationFactory();
	}

	/**
	 * Returns the modified client configuration.
	 */
	public EngineConfiguration getClientEngineConfig() {
		final BasicClientConfig cfg = new BasicClientConfig();
		cfg.deployTransport("http", new SimpleTargetedChain(new CommonsHTTPSender()));
		return cfg;
	}

	/**
	 * Returns a new basic server configuration.
	 */
	public EngineConfiguration getServerEngineConfig() {
		return new BasicServerConfig();
	}

}
