package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws611.TrackingPortTypeProxy;

public class RunnerTrackingSCIC {
	private static final Logger	LOG	= Logger.getLogger(RunnerTrackingSCIC.class.getCanonicalName());

	public static void main(final String[] args) {
		trackDetailClick(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackCart(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackBuy(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackRecEngineClick(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackSearchFeedback(Settings.getUrl(WebServiceUrlType.TRACKING));
		trackLogin(Settings.getUrl(WebServiceUrlType.TRACKING));

	}

	private static void trackDetailClick(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			// general information
			final String2StringMapEntry id = new String2StringMapEntry("id", "3865");
			// channel id will be passed below
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry event = new String2StringMapEntry("event", "click");
			final String2StringMapEntry title = new String2StringMapEntry("title", "BMC streetfire SSW");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "user123");

			// event specific information
			final String2StringMapEntry query = new String2StringMapEntry("query", "mountain bike");
			final String2StringMapEntry pos = new String2StringMapEntry("pos", "28");
			final String2StringMapEntry origPos = new String2StringMapEntry("origPos", "28");
			final String2StringMapEntry page = new String2StringMapEntry("page", "3");
			final String2StringMapEntry pageSize = new String2StringMapEntry("pageSize", "12");
			final String2StringMapEntry origPageSize = new String2StringMapEntry("origPageSize", "12");
			final String2StringMapEntry simi = new String2StringMapEntry("simi", "99.41");

			final String2StringMapEntry[] parameters = {event, id, title, sid, userId, query, pos, origPos, pageSize, origPageSize, page, simi};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information 'detail click' logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void trackBuy(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			// general information
			final String2StringMapEntry id = new String2StringMapEntry("id", "3865");
			// channel id will be passed below
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry event = new String2StringMapEntry("event", "cart");
			final String2StringMapEntry title = new String2StringMapEntry("title", "BMC streetfire SSW");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "user123");

			// event specific information
			final String2StringMapEntry count = new String2StringMapEntry("count", "48");
			final String2StringMapEntry price = new String2StringMapEntry("price", "1499");

			final String2StringMapEntry[] parameters = {event, id, title, sid, userId, count, price};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information 'cart' logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void trackCart(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			// general information
			final String2StringMapEntry id = new String2StringMapEntry("id", "3865");
			// channel id will be passed below
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry event = new String2StringMapEntry("event", "checkout");
			final String2StringMapEntry title = new String2StringMapEntry("title", "BMC streetfire SSW");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "user123");

			// event specific information
			final String2StringMapEntry count = new String2StringMapEntry("count", "48");
			final String2StringMapEntry price = new String2StringMapEntry("price", "1499");

			final String2StringMapEntry[] parameters = {event, id, title, sid, userId, count, price};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information 'checkout' logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void trackRecEngineClick(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			// general information
			final String2StringMapEntry id = new String2StringMapEntry("id", "3865");
			// channel id will be passed below
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry event = new String2StringMapEntry("event", "recommendationClick");
			final String2StringMapEntry title = new String2StringMapEntry("title", "BMC streetfire SSW");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "user123");

			// event specific information
			final String2StringMapEntry mainId = new String2StringMapEntry("mainId", "4848");

			final String2StringMapEntry[] parameters = {event, id, title, sid, userId, mainId};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information 'recommendationClick' logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void trackSearchFeedback(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			// general information
			final String2StringMapEntry id = new String2StringMapEntry("id", "3865");
			// channel id will be passed below
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry event = new String2StringMapEntry("event", "feedback");
			final String2StringMapEntry title = new String2StringMapEntry("title", "BMC streetfire SSW");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "user123");

			// event specific information
			final String2StringMapEntry query = new String2StringMapEntry("query", "mountain bike");
			final String2StringMapEntry positive = new String2StringMapEntry("positive", "true");
			final String2StringMapEntry message = new String2StringMapEntry("message", "great products, I really found what I was looking for.");

			final String2StringMapEntry[] parameters = {event, id, title, sid, userId, query, positive, message};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information 'feedback' logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}

	private static void trackLogin(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			// channel id will be passed below
			final String2StringMapEntry event = new String2StringMapEntry("event", "login");
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "user123");

			final String2StringMapEntry[] parameters = {event, sid, userId};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information 'login' logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(null, e);
		}
	}
}
