package de.factfinder.runner;

import org.apache.log4j.Logger;

import de.factfinder.adapters.wsclient.ws69.ImportPortTypeProxy;
import de.factfinder.adapters.wsclient.ws69.String2StringMapEntry;
import de.factfinder.wsclient.ws69.AuthenticationToken;
import de.factfinder.wsclient.ws69.indexer.ImportRecord;

public class RunnerImport {

	private static final Logger	LOG	= Logger.getLogger(RunnerImport.class.getCanonicalName());

	public static void main(final String[] args) {
		sendRequest(Settings.getUrl(WebServiceUrlType.IMPORT));
	}

	private static void sendRequest(final String endpoint) {
		final ImportPortTypeProxy proxy = new ImportPortTypeProxy(endpoint);

		try {
			final ImportRecord importRecord = new ImportRecord();
			final String2StringMapEntry[] map = new String2StringMapEntry[6];
			importRecord.setId("123");
			final String2StringMapEntry entryTitel = new String2StringMapEntry();
			entryTitel.setKey("Name");
			entryTitel.setValue("Produktname");
			map[0] = entryTitel;
			final String2StringMapEntry entryHersteller = new String2StringMapEntry();
			entryHersteller.setKey("Brand");
			entryHersteller.setValue("Herstellername");
			map[1] = entryHersteller;
			final String2StringMapEntry entryEAN = new String2StringMapEntry();
			entryEAN.setKey("EAN");
			entryEAN.setValue("1234567891320");
			map[2] = entryEAN;
			final String2StringMapEntry entryArtikelnummer = new String2StringMapEntry();
			entryArtikelnummer.setKey("ArticleNumber");
			entryArtikelnummer.setValue("24960999");
			map[2] = entryArtikelnummer;
			final String2StringMapEntry entryBeschreibung = new String2StringMapEntry();
			entryBeschreibung.setKey("Description");
			entryBeschreibung.setValue("Produktbeschreibung");
			map[2] = entryBeschreibung;
			final String2StringMapEntry entryPreis = new String2StringMapEntry();
			entryPreis.setKey("Price");
			entryPreis.setValue("10");
			map[2] = entryPreis;
			importRecord.setRecord(map);

			final ImportRecord importRecordUpdate = new ImportRecord();
			final String2StringMapEntry[] mapUpdate = new String2StringMapEntry[1];
			importRecordUpdate.setId("123");
			final String2StringMapEntry entryTitelUpdate = new String2StringMapEntry();
			entryTitelUpdate.setKey("Name");
			entryTitelUpdate.setValue("Mustertitel");
			mapUpdate[0] = entryTitel;
			importRecordUpdate.setRecord(mapUpdate);

			final String channel = Settings.getChannel();
			final AuthenticationToken token = Settings.getAuthToken();

			proxy.insertRecord(importRecord, channel, false, token);

			proxy.updateRecord(importRecordUpdate, channel, false, token);

			proxy.deleteRecord("123", channel, false, token);

			proxy.startImport(channel, token);
			proxy.startSuggestImport(channel, token);
			LOG.info("Record Imported Successfully");

		} catch (final Exception e) {
			LOG.error(e);
		}
	}
}
