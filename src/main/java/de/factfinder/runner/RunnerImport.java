package de.factfinder.runner;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws610.ImportPortTypeProxy;
import de.factfinder.adapters.wsclient.ws610.String2StringMapEntry;
import de.factfinder.wsclient.ws610.AuthenticationToken;
import de.factfinder.wsclient.ws610.indexer.ImportRecord;

public class RunnerImport {
	private static final Logger	LOG	= Logger.getLogger(RunnerImport.class.getCanonicalName());

	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.IMPORT));
	}

	private static void sendRequest(final String endpoint) {
		final ImportPortTypeProxy proxy = new ImportPortTypeProxy(endpoint);

		try {
			final ImportRecord importRecordUpdate = new ImportRecord();
			final String2StringMapEntry[] mapUpdate = new String2StringMapEntry[1];
			importRecordUpdate.setId("123");

			final String2StringMapEntry entryTitelUpdate = new String2StringMapEntry();
			entryTitelUpdate.setKey("Name");
			entryTitelUpdate.setValue("Mustertitel");
			mapUpdate[0] = entryTitelUpdate;
			importRecordUpdate.setRecord(mapUpdate);

			final String channel = Settings.getChannel();
			final AuthenticationToken token = Settings.getAuthToken();
			ImportRecord record = new ImportRecord();

			LOG.info("Insert record");
			record.setId("123");
			record.setRecord(map(	entry("Name", "Name Of the Product"), entry("Brand", "Brand name of the product"), entry("EAN", "1234567891320"),
									entry("ArticleNumber", "24960999"), entry("Description", "Produktbeschreibung"), entry("Price", "10")));
			proxy.insertRecord(record, channel, false, token);

			LOG.info("Update record");
			record = new ImportRecord();
			record.setId("123");
			record.setRecord(map(entry("Name", "New title of the product")));
			proxy.updateRecord(record, channel, false, token);

			LOG.info("Delete record");
			proxy.deleteRecord("123", channel, false, token);

			LOG.info("Start product data import");
			proxy.startImport(channel, token);

			LOG.info("Start suggest data import");
			proxy.startSuggestImport(channel, token);

			LOG.info("All finished successfully");
		} catch (final Exception e) {
			LOG.error(null, e);
		}
	}

	private static String2StringMapEntry entry(final String key, final String value) {
		final String2StringMapEntry entryTitel = new String2StringMapEntry();
		entryTitel.setKey(key);
		entryTitel.setValue(value);
		return entryTitel;
	}

	private static String2StringMapEntry[] map(final String2StringMapEntry... values) {
		return values;
	}
}
