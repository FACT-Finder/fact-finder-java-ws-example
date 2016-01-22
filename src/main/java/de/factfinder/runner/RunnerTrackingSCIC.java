package de.factfinder.runner;

import static de.factfinder.runner.util.Helper.newEntry;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.LogInformation;
import de.factfinder.adapters.wsclient.ws611.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws611.TrackingPortType;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws611.AuthenticationToken;

public class RunnerTrackingSCIC {
	private static final Logger					LOG		= Logger.getLogger(RunnerTrackingSCIC.class.getCanonicalName());
	private static final AuthenticationToken	TOKEN	= Settings.getAuthToken();
	private static final String					CHANNEL	= Settings.getChannel();

	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.TRACKING);
		final TrackingPortType proxy = Service.get(TrackingPortType.class, endpoint);
		trackDetailClick(proxy);
		trackCart(proxy);
		trackBuy(proxy);
		trackRecEngineClick(proxy);
		trackSearchFeedback(proxy);
		trackLogin(proxy);
	}

	private static void trackDetailClick(final TrackingPortType proxy) {
		// general information
		final List<String2StringMapEntry> map = new ArrayList<>();

		// general information
		map.add(newEntry("id", "3865"));
		// channel id will be passed below
		map.add(newEntry("sid", "abc123def456ghi789"));
		map.add(newEntry("event", "click"));
		map.add(newEntry("title", "BMC streetfire SSW"));
		map.add(newEntry("userId", "user123"));
		// event specific information
		map.add(newEntry("query", "mountain bike"));
		map.add(newEntry("pos", "28"));
		map.add(newEntry("origPos", "28"));
		map.add(newEntry("page", "3"));
		map.add(newEntry("pageSize", "12"));
		map.add(newEntry("origPageSize", "12"));
		map.add(newEntry("simi", "99.41"));

		final boolean status = logInformation(proxy, map);
		LOG.info("Tracking information 'detail click' logging status: " + status);
	}

	private static void trackBuy(final TrackingPortType proxy) {
		final List<String2StringMapEntry> map = new ArrayList<>();

		// general information
		map.add(newEntry("id", "3865"));
		// channel id will be passed below
		map.add(newEntry("sid", "abc123def456ghi789"));
		map.add(newEntry("event", "cart"));
		map.add(newEntry("title", "BMC streetfire SSW"));
		map.add(newEntry("userId", "user123"));

		// event specific information
		map.add(newEntry("count", "48"));
		map.add(newEntry("price", "1499"));

		final boolean status = logInformation(proxy, map);
		LOG.info("Tracking information 'cart' logging status: " + status);
	}

	private static void trackCart(final TrackingPortType proxy) {
		final List<String2StringMapEntry> map = new ArrayList<>();
		// general information
		map.add(newEntry("id", "3865"));
		// channel id will be passed below
		map.add(newEntry("sid", "abc123def456ghi789"));
		map.add(newEntry("event", "checkout"));
		map.add(newEntry("title", "BMC streetfire SSW"));
		map.add(newEntry("userId", "user123"));

		// event specific information
		map.add(newEntry("count", "48"));
		map.add(newEntry("price", "1499"));

		final boolean status = logInformation(proxy, map);
		LOG.info("Tracking information 'checkout' logging status: " + status);
	}

	private static void trackRecEngineClick(final TrackingPortType proxy) {
		final List<String2StringMapEntry> map = new ArrayList<>();
		// general information
		map.add(newEntry("id", "3865"));
		// channel id will be passed below
		map.add(newEntry("sid", "abc123def456ghi789"));
		map.add(newEntry("event", "recommendationClick"));
		map.add(newEntry("title", "BMC streetfire SSW"));
		map.add(newEntry("userId", "user123"));

		// event specific information
		map.add(newEntry("mainId", "4848"));

		final boolean status = logInformation(proxy, map);
		LOG.info("Tracking information 'recommendationClick' logging status: " + status);

	}

	private static void trackSearchFeedback(final TrackingPortType proxy) {
		final List<String2StringMapEntry> map = new ArrayList<>();
		// general information
		map.add(newEntry("id", "3865"));
		// channel id will be passed below
		map.add(newEntry("sid", "abc123def456ghi789"));
		map.add(newEntry("event", "feedback"));
		map.add(newEntry("title", "BMC streetfire SSW"));
		map.add(newEntry("userId", "user123"));

		// event specific information
		map.add(newEntry("query", "mountain bike"));
		map.add(newEntry("positive", "true"));
		map.add(newEntry("message", "great products, I really found what I was looking for."));

		final boolean status = logInformation(proxy, map);
		LOG.info("Tracking information 'feedback' logging status: " + status);

	}

	private static void trackLogin(final TrackingPortType proxy) {
		final List<String2StringMapEntry> map = new ArrayList<>();

		// channel id will be passed below
		map.add(newEntry("event", "login"));
		map.add(newEntry("sid", "abc123def456ghi789"));
		map.add(newEntry("userId", "user123"));

		final boolean status = logInformation(proxy, map);
		LOG.info("Tracking information 'login' logging status: " + status);
	}

	private static boolean logInformation(TrackingPortType proxy, List<String2StringMapEntry> map) {
		LogInformation log = new LogInformation();
		log.setIn0(CHANNEL);
		log.setIn1(map);
		log.setIn2(TOKEN);
		return proxy.logInformation(log).isOut();
	}
}
