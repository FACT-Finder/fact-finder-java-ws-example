package de.factfinder.runner;

import static de.factfinder.runner.util.Helper.newEntry;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws71.LogInformation;
import de.factfinder.adapters.wsclient.ws71.LogMultipleInformation;
import de.factfinder.adapters.wsclient.ws71.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws71.TrackingPortType;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws71.AuthenticationToken;
import de.factfinder.wsclient.ws71.suggest.TrackingEvent;

public class RunnerTrackingAD {
	private static final Logger					LOG			= Logger.getLogger(RunnerTrackingAD.class.getCanonicalName());

	private static final String					CHANNEL		= Settings.getChannel();
	private static final AuthenticationToken	TOKEN		= Settings.getAuthToken();

	private static final String					SHOP_USER	= "demoUser";
	private static final String					COOKIE_ID	= "cookie";
	private static final String					SESSION_ID	= "abc123def456ghi789";
	private static final String					REF_KEY_1	= "DMgnzi6zw";
	private static final String					REF_KEY_2	= "J5YMlyY1f";

	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.TRACKING);
		final TrackingPortType proxy = Service.get(TrackingPortType.class, endpoint);
		trackSessionInfo(proxy);
		trackInspect(proxy);
		trackBulkBuy(proxy);
	}

	private static void trackSessionInfo(final TrackingPortType proxy) {
		List<String2StringMapEntry> entries = new ArrayList<>();
		entries.add(newEntry("event", "sessionInfo"));
		entries.add(newEntry("channel", "de"));
		entries.add(newEntry("site", "wsdemo"));
		entries.add(newEntry("sid", SESSION_ID));
		entries.add(newEntry("cid", COOKIE_ID));
		entries.add(newEntry("uid", SHOP_USER));

		LogInformation log = new LogInformation();
		log.setIn0(Settings.getChannel());
		log.setIn1(entries);
		log.setIn2(Settings.getAuthToken());

		final boolean status = proxy.logInformation(log).isOut();
		LOG.info("Tracking information logging status: " + status);
	}

	private static void trackInspect(final TrackingPortType proxy) {
		List<String2StringMapEntry> entries = new ArrayList<>();
		entries.add(newEntry("event", "inspect"));
		entries.add(newEntry("channel", "de"));
		entries.add(newEntry("sourceRef", REF_KEY_1));
		entries.add(newEntry("id", "1234-56"));
		entries.add(newEntry("sid", SESSION_ID));
		entries.add(newEntry("query", "test"));
		entries.add(newEntry("origPos", "4"));
		entries.add(newEntry("origPageSize", "2"));

		LogInformation log = new LogInformation();
		log.setIn0(Settings.getChannel());
		log.setIn1(entries);
		log.setIn2(Settings.getAuthToken());

		final boolean status = proxy.logInformation(log).isOut();
		LOG.info("Tracking information logging status: " + status);
	}

	private static void trackBulkBuy(final TrackingPortType proxy) {
		final List<TrackingEvent> entries = new ArrayList<>();
		entries.add(getTrackingEvent("buy", CHANNEL, SESSION_ID, REF_KEY_1, "3364790", "M3364790", "1", "10", "1"));
		entries.add(getTrackingEvent("buy", CHANNEL, SESSION_ID, REF_KEY_2, "66486423", "M46999467", "10", "1.99", "2"));
		entries.add(getTrackingEvent("buy", CHANNEL, SESSION_ID, REF_KEY_2, "29986254", "M11289556", "2", "9.99", "3"));
		entries.add(getTrackingEvent("buy", CHANNEL, SESSION_ID, REF_KEY_2, "29986254", "M11289556", "2", "9.99", "3"));

		final LogMultipleInformation log = new LogMultipleInformation();
		log.setIn0(CHANNEL);
		log.setIn1(entries);
		log.setIn2(TOKEN);

		final boolean status = proxy.logMultipleInformation(log).isOut();
		LOG.info("Tracking multiple information logging status: " + status);
	}

	private static TrackingEvent getTrackingEvent(final String interestType, final String channel, final String sid, final String sourceRef, final String id,
			final String mid, final String amount, final String price, final String count) {
		final TrackingEvent event = new TrackingEvent();
		final List<String2StringMapEntry> entries = new ArrayList<>();
		entries.add(newEntry("event", interestType));
		entries.add(newEntry("channel", channel));
		entries.add(newEntry("sid", sid));
		entries.add(newEntry("sourceRef", sourceRef));
		entries.add(newEntry("id", id));
		entries.add(newEntry("mid", mid));
		entries.add(newEntry("amount", amount));
		entries.add(newEntry("price", price));
		entries.add(newEntry("count", count));
		event.setParameters(entries);
		return event;
	}
}
