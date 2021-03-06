/**
 *  Copyright 2014 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 *  Copyright 2014 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.europeana.querylog.cli;

import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;
import it.cnr.isti.hpc.io.reader.JsonRecordParser;
import it.cnr.isti.hpc.io.reader.RecordReader;
import it.cnr.isti.hpc.log.ProgressLogger;
import eu.europeana.querylog.BotFilter;
import eu.europeana.querylog.EuropeanaRecord;
import eu.europeana.querylog.HasDateFilter;
import eu.europeana.querylog.HasQueryFilter;

/**
 * Converts a json-encoded query log to a tab-separated-values query log, note
 * that log record not containing a query, performed by bots or with no date
 * will be removed. You can run this command with: <br/>
 * <br/>
 * <code> java eu.europeana.querylog.cli.EuropeanaJsonLogsToTsvCLI -input log.json[.gz] -output log.tsv[.gz] </code>
 * <br/>
 * <br/>
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 1, 2014
 */
public class EuropeanaJsonLogsToTsvCLI extends AbstractCommandLineInterface {

	public EuropeanaJsonLogsToTsvCLI(String[] args) {
		super(args);
	}

	public static void main(String[] args) {
		EuropeanaJsonLogsToTsvCLI cli = new EuropeanaJsonLogsToTsvCLI(args);
		JsonRecordParser<EuropeanaRecord> parser = new JsonRecordParser<EuropeanaRecord>(
				EuropeanaRecord.class);
		RecordReader<EuropeanaRecord> reader = new RecordReader<EuropeanaRecord>(
				cli.getInput(), parser).filter(new HasQueryFilter(),
				new HasDateFilter(), new BotFilter());
		ProgressLogger pl = new ProgressLogger("reader {} records", 100);
		cli.openOutput();
		for (EuropeanaRecord er : reader) {
			pl.up();
			cli.writeLineInOutput(er.toTsv());
		}
		cli.closeOutput();

	}

}
