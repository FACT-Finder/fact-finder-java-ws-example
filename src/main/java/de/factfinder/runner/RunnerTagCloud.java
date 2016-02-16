package de.factfinder.runner;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws71.GetTagCloudEntries;
import de.factfinder.adapters.wsclient.ws71.TagcloudPortType;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws71.whatshot.TagCloud;
import de.factfinder.wsclient.ws71.whatshot.TagCloudEntry;

public class RunnerTagCloud {
	private static final Logger	LOG	= Logger.getLogger(RunnerTagCloud.class.getCanonicalName());

	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.TAGCLOUD);
		final TagcloudPortType proxy = Service.get(TagcloudPortType.class, endpoint);

		LOG.info("=== BEGIN TAGCLOUD ENTRIES ===");
		GetTagCloudEntries get = new GetTagCloudEntries();
		get.setIn0(Settings.getChannel());
		get.setIn1(10);
		get.setIn2(Settings.getAuthToken());

		final TagCloud result = proxy.getTagCloudEntries(get).getOut();
		for (final TagCloudEntry entry : result.getEntries()) {
			LOG.info("entry term=[" + entry.getSearchTerm() + "], weight=[" + entry.getWeight() + "]");
		}
		LOG.info("=== END TAGCLOUD ENTRIES ===");
	}
}
