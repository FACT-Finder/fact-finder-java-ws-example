package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws610.TrackingPortTypeProxy;
import de.factfinder.wsclient.ws610.suggest.TrackingEvent;

public class RunnerADTracking {
	private static final Logger	LOG			= Logger.getLogger(RunnerADTracking.class.getCanonicalName());

	private static final String	SHOP_USER	= "demoUser";
	private static final String	COOKIE_ID	= "cookie";
	private static final String	SESSION_ID	= "abc123def456ghi789";
	private static final String	REF_KEY_1	= "DMgnzi6zw";
	private static final String	REF_KEY_2	= "J5YMlyY1f";

	public static void main(final String[] args) {
		trackSessionInfo(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackInspect(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackBulkBuy(Settings.getUrl(WebServiceUrlType.TRACKING));
	}

	private static void trackSessionInfo(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			final String2StringMapEntry event = new String2StringMapEntry("event", "sessionInfo");
			final String2StringMapEntry channel = new String2StringMapEntry("channel", "de");
			final String2StringMapEntry site = new String2StringMapEntry("site", "wsdemo");
			final String2StringMapEntry sid = new String2StringMapEntry("sid", SESSION_ID);
			final String2StringMapEntry cid = new String2StringMapEntry("cid", COOKIE_ID);
			final String2StringMapEntry uid = new String2StringMapEntry("uid", SHOP_USER);

			final String2StringMapEntry[] parameters = {event, channel, site, sid, cid, uid};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	private static void trackInspect(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			final String2StringMapEntry event = new String2StringMapEntry("event", "inspect");
			final String2StringMapEntry channel = new String2StringMapEntry("channel", "de");
			final String2StringMapEntry sourceRef = new String2StringMapEntry("sourceRef", REF_KEY_1);
			final String2StringMapEntry id = new String2StringMapEntry("id", "1234-56");
			final String2StringMapEntry sid = new String2StringMapEntry("sid", SESSION_ID);

			final String2StringMapEntry[] parameters = {event, sourceRef, channel, id, sid};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	private static void trackBulkBuy(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);
		final String channel = Settings.getChannel();

		try {
			// first buy event
			final String2StringMapEntry[] parameters1 = getInterestParameters("buy", channel, SESSION_ID, REF_KEY_1, "3364790", "M3364790", "1", "10");

			// second buy event
			final String2StringMapEntry[] parameters2 = getInterestParameters("buy", channel, SESSION_ID, REF_KEY_2, "66486423", "M46999467", "10", "1.99");

			// third buy event
			final String2StringMapEntry[] parameters3 = getInterestParameters("buy", channel, SESSION_ID, REF_KEY_2, "29986254", "M11289556", "2", "9.99");

			final TrackingEvent[] trackingEvents = {new TrackingEvent(parameters1), new TrackingEvent(parameters2), new TrackingEvent(parameters3)};

			final boolean status = tptp.logMultipleInformation(channel, trackingEvents, Settings.getAuthToken());
			LOG.info("Tracking multiple information logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}

	private static String2StringMapEntry[] getInterestParameters(final String interestType, final String channel, final String sid, final String sourceRef,
			final String id, final String mid, final String amount, final String price) {
		final String2StringMapEntry eventEntry = new String2StringMapEntry("event", interestType);
		final String2StringMapEntry channelEntry = new String2StringMapEntry("channel", channel);
		final String2StringMapEntry sidEntry = new String2StringMapEntry("sid", sid);
		final String2StringMapEntry sourceRefEntry = new String2StringMapEntry("sourceRef", sourceRef);
		final String2StringMapEntry idEntry = new String2StringMapEntry("id", id);
		final String2StringMapEntry midEntry = new String2StringMapEntry("mid", mid);
		final String2StringMapEntry amountEntry = new String2StringMapEntry("amount", amount);
		final String2StringMapEntry priceEntry = new String2StringMapEntry("price", price);

		final String2StringMapEntry[] interestParameters = {eventEntry, channelEntry, sidEntry, sourceRefEntry, idEntry, midEntry, amountEntry, priceEntry};
		return interestParameters;
	}
}
