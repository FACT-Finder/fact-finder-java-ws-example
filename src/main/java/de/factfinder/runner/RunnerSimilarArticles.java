package de.factfinder.runner;


import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws71.GetSimilarProducts;
import de.factfinder.adapters.wsclient.ws71.SimilarProductsPortType;
import de.factfinder.runner.print.SearchResultInformationPrinter;
import de.factfinder.runner.util.Service;
import de.factfinder.wsclient.ws71.AuthenticationToken;
import de.factfinder.wsclient.ws71.Record;
import de.factfinder.wsclient.ws71.similarresults.SimilarRecordsResult;

public class RunnerSimilarArticles {
	private static final Logger					LOG		= Logger.getLogger(RunnerSimilarArticles.class.getCanonicalName());
	private static final String					CHANNEL	= Settings.getChannel();
	private static final AuthenticationToken	TOKEN	= Settings.getAuthToken();

	public static void main(final String[] args) {
		final String endpoint = Settings.getUrl(WebServiceUrlType.SIMILAR_ARTICLES);
		final SimilarProductsPortType proxy = Service.get(SimilarProductsPortType.class, endpoint);
		final SearchResultInformationPrinter srip = new SearchResultInformationPrinter();

		final int maxArticles = 10;
		final String recordId = "1458";

		LOG.info("=== BEGIN SIMILAR ARTICLES (NORMAL) ===");
		SimilarRecordsResult srr = getSimilarProducts(proxy, recordId, false, maxArticles);
		for (final Record rec : srr.getRecords()) {
			srip.printRecord(rec, "");
		}
		LOG.info("=== END SIMILAR ARTICLES (NORMAL) ===");

		LOG.info("=== BEGIN SIMILAR ARTICLES (IDs ONLY) ===");
		srr = getSimilarProducts(proxy, recordId, true, maxArticles);
		for (final Record rec : srr.getRecords()) {
			srip.printRecord(rec, "");
		}
		LOG.info("=== END SIMILAR ARTICLES (IDs ONLY) ===");
	}

	private static SimilarRecordsResult getSimilarProducts(SimilarProductsPortType proxy, String recordId, boolean idsOnly, int maxArticles) {
		GetSimilarProducts get = new GetSimilarProducts();
		get.setIn0(CHANNEL);
		get.setIn1(recordId);
		get.setIn2(idsOnly);
		get.setIn3(maxArticles);
		get.setIn4(TOKEN);
		return proxy.getSimilarProducts(get).getOut();
	}
}