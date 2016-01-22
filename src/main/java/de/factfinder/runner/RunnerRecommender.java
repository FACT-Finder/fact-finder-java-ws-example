package de.factfinder.runner;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws611.GetRecommendation;
import de.factfinder.adapters.wsclient.ws611.GetRecommendation1;
import de.factfinder.adapters.wsclient.ws611.ImportData1;
import de.factfinder.adapters.wsclient.ws611.RecommenderPortType;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws611.AuthenticationToken;
import de.factfinder.wsclient.ws611.Record;


public class RunnerRecommender {
	private static final Logger					LOG					= Logger.getLogger(RunnerRecommender.class.getCanonicalName());
	private static final int					MAX_RETURN_RECORDS	= 10;
	private static final String					CHANNEL				= Settings.getChannel();
	private static final AuthenticationToken	TOKEN				= Settings.getAuthToken();

	public static void main(final String[] args) throws MalformedURLException {
		final String endpoint = Settings.getUrl(WebServiceUrlType.RECOMMENDER);
		final RecommenderPortType proxy = Service.get(RecommenderPortType.class, endpoint);
		final SearchResultInformationPrinter searchResultInfoPrinter = new SearchResultInformationPrinter();

		importDatabase(proxy, CHANNEL, TOKEN);

		final String productMatchOnFieldValue1 = "1892333";

		LOG.info("==== BEGIN RECOMMENDATION RESULT (NORMAL) ====");
		final GetRecommendation get = new GetRecommendation();
		get.setIn0(MAX_RETURN_RECORDS);
		get.setIn1(productMatchOnFieldValue1);
		get.setIn2(CHANNEL);
		get.setIn3(false);
		get.setIn4(TOKEN);
		List<Record> records = proxy.getRecommendation(get).getOut().getResultRecords();
		printResult(searchResultInfoPrinter, records);
		LOG.info("==== END RECOMMENDATION RESULT (NORMAL) ====");

		LOG.info("==== BEGIN RECOMMENDATION RESULT (IDs ONLY) ====");
		get.setIn3(true);
		// NOTE: when using the idsOnly-mode, the returned record-id is the value from the matchOnField which might not be the normal record-id.
		records = proxy.getRecommendation(get).getOut().getResultRecords();
		LOG.info(records.toString());
		LOG.info(records.toString());
		printResult(searchResultInfoPrinter, records);
		LOG.info("==== END RECOMMENDATION RESULT (IDs ONLY) ====");

		LOG.info("==== BEGIN RECOMMENDATION RESULT (MULTI PRODUCTS; IDs ONLY) ====");
		final String productMatchOnFieldValue2 = "1715749";
		final GetRecommendation1 get2 = new GetRecommendation1();
		get2.setIn0(MAX_RETURN_RECORDS);
		get2.setIn1(Arrays.asList(productMatchOnFieldValue1, productMatchOnFieldValue2));
		get2.setIn2(CHANNEL);
		get2.setIn3(true);
		get2.setIn4(TOKEN);

		records = proxy.getRecommendation1(get2).getOut().getResultRecords();
		printResult(searchResultInfoPrinter, records);
		LOG.info("==== END RECOMMENDATION RESULT (MULTI PRODUCTS; IDs ONLY) ====");

	}

	private static void printResult(final SearchResultInformationPrinter searchResultInfoPrinter, final List<Record> records) {
		if (records != null && !records.isEmpty()) {
			for (final Record rec : records) {
				searchResultInfoPrinter.printRecord(rec, "");
			}
		} else {
			LOG.info("No recommendations received.");
		}
	}

	private static void importDatabase(final RecommenderPortType proxy, final String channel, final AuthenticationToken token) {
		final ImportData1 i = new ImportData1();
		i.setIn0(channel);
		i.setIn1(true);
		i.setIn2(TOKEN);
		final boolean success = proxy.importData1(i).isOut();
		LOG.info("RecommenderEngine database for channel '" + channel + "' has been imported. Status: " + success);
	}
}
