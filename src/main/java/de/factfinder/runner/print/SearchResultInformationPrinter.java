package de.factfinder.runner.print;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws69.String2StringMapEntry;
import de.factfinder.wsclient.ws69.Record;
import de.factfinder.wsclient.ws69.search.Filter;
import de.factfinder.wsclient.ws69.search.FilterValue;
import de.factfinder.wsclient.ws69.search.Group;
import de.factfinder.wsclient.ws69.search.GroupElement;
import de.factfinder.wsclient.ws69.search.Params;
import de.factfinder.wsclient.ws69.search.Result;
import de.factfinder.wsclient.ws69.search.SearchRecord;
import de.factfinder.wsclient.ws69.search.SortItem;

/**
 * Prints information about the search result.
 */
public final class SearchResultInformationPrinter {
	private final static Logger	LOG	= Logger.getLogger(SearchResultInformationPrinter.class);

	/**
	 * Prints the search parameters.
	 * 
	 * @param params The search parameters.
	 */
	public void printSearchParameters(final Params params) {
		LOG.info("Channel: [" + params.getChannel() + "]");

		LOG.info("Query: [" + params.getQuery() + "]");

		LOG.info("Article number: [" + params.getArticleNumber() + "] (Don't know if this is still in use. See FFFCORE-64)");

		LOG.info("No article number search: [" + params.getNoArticleNumberSearch()
				+ "] (if the query looks like an article number but FACT-Finder should perform a normal search)");

		LOG.info("Result page: [" + params.getPage() + "] (if there are more hits than set in 'records per page', "
				+ "the result is split into several pages. By setting this parameter you can get the records for a specific page.)");

		// log.info("Random seed: [" + params.getRandomSeed() + "] (this parameter is no longer used and will be removed in a future release. See FFFCORE-63)");

		LOG.info("Records per page: [" + params.getResultsPerPage() + "]");

		LOG.info("Search field: [" + params.getSearchField() + "] (by default FACT-Finder searches in all fields "
				+ "in the database but you can set this parameter to let it search only in the specified field)");

		// Print sort options
		if (params.getSortsList() != null) {
			final StringBuilder msg = new StringBuilder("Sort options:");
			for (final SortItem item : params.getSortsList()) {
				msg.append("\n\tname=[").append(item.getName()).append("], order=[").append(item.getOrder());
				if (item.getSelected() != null && item.getSelected()) {
					msg.append(" (selected)");
				}
			}
			LOG.info(msg.toString());
		}

		// Print filters
		if (params.getFilters() != null) {
			final StringBuilder msg = new StringBuilder("Search filters:");
			for (final Filter filter : params.getFilters()) {
				msg.append("\n\tname=[").append(filter.getName()).append("]");

				// If this filter is set to a single value: manufacturer="Atari"
				if (filter.getValueList() != null) {
					msg.append(", value=[").append(filter.getValueList()).append("]");
				}

				// If the filter is set to several values: manufacturer="Atari" or "Apple"
				if (filter.getValueList() != null) {
					msg.append(", multiple values:");
					for (int i = 0; i < filter.getValueList().length; i++) {
						final FilterValue value = filter.getValueList()[i];
						if (i > 0) {
							msg.append(" ").append(value.getType().getValue());
						}
						msg.append(" ").append(value.getValue());
					}
				}
			}
			LOG.info(msg.toString());
		}
	}

	/**
	 * Prints a single search record.
	 * 
	 * @param record The record.
	 */
	public void printSearchRecord(final SearchRecord record) {
		final StringBuilder recordString = printString2StringMap(record.getRecord());
		LOG.info("Record #" + record.getPosition() + ": " + recordString.toString());
	}

	/**
	 * Prints a single search record.
	 * 
	 * @param record The record.
	 * @param prefix text which will be appended in front.
	 */
	public void printRecord(final Record record, final String prefix) {
		final StringBuilder recordString = printString2StringMap(record.getRecord());
		LOG.info(prefix + "Record: " + recordString.toString());
	}

	/**
	 * Prints the search result.
	 * 
	 * @param result The search result.
	 */
	public void printSearchResult(final Result result) {
		LOG.info("Status: [" + result.getResultStatus() + "] (one of \'resultsFound\', \'nothingFound\', \'errorOccured\')");
		LOG.info("Hit count: " + result.getResultCount());

		// This loop doesn't get all records. It only returns those which are present on the current page. The number of results per page can be set in the
		// search parameters
		LOG.info("Hits on page #" + result.getPaging().getCurrentPage());
		for (final SearchRecord record : result.getRecords()) {
			printSearchRecord(record);
		}
	}

	/**
	 * Prints the After Search Navigation (ASN).
	 * 
	 * @param result The search result.
	 */
	public void printAfterSearchNavigation(final Result result) {
		LOG.info("Number of groups: " + result.getGroups().length);
		for (final Group group : result.getGroups()) {
			String groupMsg = "The group [" + group.getName() + "] contains " + group.getElements().length + " elements and is ";
			if (group.getSelectedElements().length == 0) {
				groupMsg += "not ";
			}
			groupMsg += "selected";
			LOG.info(groupMsg);

			String unit = group.getUnit();
			if (!unit.isEmpty()) {
				unit = " " + unit;
			}
			// Print the group elements and mark them as selected if applicable
			for (final GroupElement element : group.getElements()) {
				String elementMsg = "      " + element.getName() + unit + " (" + element.getRecordCount() + ")";
				if (element.getSelected()) {
					elementMsg += " <= selected";
				}
				LOG.info(elementMsg);
			}
		}
	}

	private StringBuilder printString2StringMap(final String2StringMapEntry[] string2StringMap) {
		final StringBuilder recordString = new StringBuilder();
		for (final String2StringMapEntry entry : string2StringMap) {
			if (recordString.length() > 0) {
				recordString.append(", ");
			}
			recordString.append(entry.getKey()).append("=[").append(entry.getValue()).append("]");
		}
		return recordString;
	}
}
