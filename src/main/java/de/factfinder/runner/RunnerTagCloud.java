package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.TagcloudPortTypeProxy;
import de.factfinder.wsclient.ws611.whatshot.TagCloud;
import de.factfinder.wsclient.ws611.whatshot.TagCloudEntry;

public class RunnerTagCloud {
	private static final Logger	LOG	= Logger.getLogger(RunnerTagCloud.class.getCanonicalName());

	/**
	 * Runs the sample program.
	 * 
	 * <p>
	 * To run this code, fire up your search application and change the 'endpoint' variable to the URL of your application. If you don't have your own search
	 * application installed, you can use our demo shop, which is publicly accessible ({@code PUBLIC_URL}).
	 * </p>
	 * 
	 * @param args Unused.
	 */
	public static void main(final String[] args) {
		final TagcloudPortTypeProxy proxy = new TagcloudPortTypeProxy(Settings.getUrl(WebServiceUrlType.TAGCLOUD));

		try {
			final TagCloud tagCloud = proxy.getTagCloudEntries(Settings.getChannel(), 10, Settings.getAuthToken());

			LOG.info("=== BEGIN TAGCLOUD ENTRIES ===");
			for (final TagCloudEntry entry : tagCloud.getEntries()) {
				LOG.trace("entry term=[" + entry.getSearchTerm() + "], weight=[" + entry.getWeight() + "]");
			}
			LOG.info("=== END TAGCLOUD ENTRIES ===");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}
}
