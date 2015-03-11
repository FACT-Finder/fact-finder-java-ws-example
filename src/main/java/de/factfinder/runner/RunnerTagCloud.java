package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.TagcloudPortTypeProxy;
import de.factfinder.wsclient.ws611.whatshot.TagCloud;
import de.factfinder.wsclient.ws611.whatshot.TagCloudEntry;

public class RunnerTagCloud {
	private static final Logger	LOG	= Logger.getLogger(RunnerTagCloud.class.getCanonicalName());

	public static void main(final String[] args) {
		final TagcloudPortTypeProxy proxy = new TagcloudPortTypeProxy(Settings.getUrl(WebServiceUrlType.TAGCLOUD));

		try {

			LOG.info("=== BEGIN TAGCLOUD ENTRIES ===");
			final TagCloud tagCloud = proxy.getTagCloudEntries(Settings.getChannel(), 10, Settings.getAuthToken());
			for (final TagCloudEntry entry : tagCloud.getEntries()) {
				LOG.info("entry term=[" + entry.getSearchTerm() + "], weight=[" + entry.getWeight() + "]");
			}
			LOG.info("=== END TAGCLOUD ENTRIES ===");

		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}
}
