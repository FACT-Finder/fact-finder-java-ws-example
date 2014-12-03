package de.factfinder.runner;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.String2StringMapEntry;
import de.factfinder.adapters.wsclient.ws610.TrackingPortTypeProxy;

public class RunnerTracking {
	private static final Logger	LOG	= Logger.getLogger(RunnerTracking.class.getCanonicalName());

	public static void main(final String[] args) {
		trackInspect(Settings.getUrl(WebServiceUrlType.TRACKING));
	}

	private static void trackInspect(final String endpoint) {
		final TrackingPortTypeProxy tptp = new TrackingPortTypeProxy(endpoint);

		try {
			final String2StringMapEntry event = new String2StringMapEntry("event", "click");
			final String2StringMapEntry id = new String2StringMapEntry("id", "3865");
			final String2StringMapEntry title = new String2StringMapEntry("title", "BMC streetfire SSW");
			final String2StringMapEntry sid = new String2StringMapEntry("sid", "abc123def456ghi789");
			final String2StringMapEntry userId = new String2StringMapEntry("userId", "abc123def456ghi789");
			final String2StringMapEntry query = new String2StringMapEntry("query", "rennrad");
			final String2StringMapEntry pos = new String2StringMapEntry("pos", "28");
			final String2StringMapEntry origPos = new String2StringMapEntry("origPos", "28");
			final String2StringMapEntry pageSize = new String2StringMapEntry("pageSize", "12");
			final String2StringMapEntry origPageSize = new String2StringMapEntry("origPageSize", "12");
			final String2StringMapEntry page = new String2StringMapEntry("page", "3");
			final String2StringMapEntry simi = new String2StringMapEntry("simi", "99.41");
			final String2StringMapEntry count = new String2StringMapEntry("count", "48");
			final String2StringMapEntry price = new String2StringMapEntry("price", "1499");
			final String2StringMapEntry mainId = new String2StringMapEntry("mainId", "3865");
			final String2StringMapEntry[] parameters = {event, id, title, sid, userId, query, pos, origPos, pageSize, origPageSize, page, simi, count, price,
					mainId};

			final boolean status = tptp.logInformation(Settings.getChannel(), parameters, Settings.getAuthToken());
			LOG.info("Tracking information logging status: " + status);
		} catch (final RemoteException e) {
			LOG.error(e);
		}
	}
}
