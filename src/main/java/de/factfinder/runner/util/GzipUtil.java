package de.factfinder.runner.util;

import org.apache.axis.EngineConfigurationFactory;
import org.apache.axis.client.Stub;
import org.apache.axis.transport.http.HTTPConstants;

import de.factfinder.runner.RunnerSearch;

/**
 * A simple utility class to provide gzip support. To enable that we have to tell axis to use CommonsHTTPSender instead of the default value HTTPSender. There
 * are two ways to do that.
 * <p>
 * 1. There is a file called "client-config.wsdd" in the package org.apache.axis.client of the axis.jar file. We can put our own "client-config.wsdd" within the
 * same package and override the setting. This is already done please check the "client-config.wsdd" file within this project to gain more information. This
 * approach will most likely work, however there is no written guarantee that the right file will be used at runtime. The documentation on this issue is not
 * very clear.
 * </p>
 * <p>
 * 2. This approach tells axis to use different {@link EngineConfigurationFactory}. For this approach there is also already an example ready. Have a look at
 * {@link CustomEngineConfigurationFactory}. The last step which has to be done is to set a axis property with the new factory. Example can be seen within the
 * {@link RunnerSearch} class (at the top the static initialization which is commented out).
 * </p>
 */
public class GzipUtil {

	/**
	 * Private constructor.
	 */
	private GzipUtil() {
	}

	/**
	 * Enables gzip support.
	 * 
	 * @param stub instance of {@link Stub}.
	 */
	public static void setGzip(final Stub stub) {
		stub._setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);
		// Additionally it is also possible to gzip the request. However tomcat has no built-in support for that.
		// If you want to use this the server has to be able to handle this. For tomcat there is a filter which has to be configured, which decompresses the
		// request.
		// stub._setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
	}

}
