/**
 *  Copyright 2012 Diego Ceccarelli
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
package eu.europeana.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Nov 27, 2012
 */
public class SimpleCollectionTest {
	private static SimpleCollectionSolrInstance instance;

	@BeforeClass
	public static void runSolrInstance() throws SolrServerException,
			IOException {
		instance = new SimpleCollectionSolrInstance();
		instance.setSolrdir(new File(
				"./src/test/resources/solr/simple-collection"));
		instance.deleteByQuery("*:*");

		index(instance);
	}

	// index a small collection of 3 documents, just to test if the scores
	// are computed correctly
	public static void index(SimpleCollectionSolrInstance tester)
			throws SolrServerException, IOException {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("europeana_id", "0");
		doc.addField("title", "leonardo da vinci");
		doc.addField("author", "leonardo da");
		doc.addField("description", "leonardo leonardo leonardo");
		tester.add(doc);
		doc = new SolrInputDocument();
		doc.addField("europeana_id", "1");
		doc.addField("title", "leonardo ");
		doc.addField("author", "leonardo da vinci ");
		doc.addField("description", "leonardo test");
		tester.add(doc);
		doc = new SolrInputDocument();
		doc.addField("europeana_id", "2");
		doc.addField("title", "leonardo leonardo ");
		doc.addField("author", "leonardo da ");
		doc.addField("description", "leonardo leonardo leonardo leonardo vinci");
		tester.add(doc);
		doc = new SolrInputDocument();
		doc.addField("europeana_id", "3");
		doc.addField("title", "picasso");
		doc.addField("author", "pablo picasso");
		doc.addField("description", "picasso is a good guy vinci");
		tester.add(doc);
		tester.commit();
	}

	private static SolrDocumentList getResults(String query)
			throws SolrServerException {
		System.out.println("QUERY " + query);
		SolrQuery q = new SolrQuery(query);
		q.set("debugQuery", "on");
		q.set("defType", "bm25f");
		q.setRows(10);
		QueryResponse qr = instance.query(q);
		// Map<String, String> explainmap = qr.getExplainMap();
		return qr.getResults();
	}

	private static Map<String, Object> explain(String query)
			throws SolrServerException {
		System.out.println("QUERY " + query);
		SolrQuery q = new SolrQuery(query);
		q.set("debugQuery", "on");
		q.set("defType", "bm25f");
		q.setRows(10);
		QueryResponse qr = instance.query(q);
		Map<String, Object> explainmap = qr.getDebugMap();
		return explainmap;
	}

	@Test
	public void testResults() {
		try {
			SolrDocumentList results = getResults("leonardo");
			assertEquals(3, results.size());
			// // check if the status is not corrupted
			results = getResults("leonardo");
			assertEquals(3, results.size());
			results = getResults("vinci");
			assertEquals(4, results.size());
			results = getResults("test");
			assertEquals(1, results.size());
			results = getResults("thistermisnotintheindexmuahahah");
			assertEquals(0, results.size());
			results = getResults("picasso");
			assertEquals(1, results.size());
			results = getResults("vinci");
			assertEquals(4, results.size());
			results = getResults("picasso vinci");
			assertEquals(1, results.size());
			results = getResults("picasso vinci leonardo");
			assertEquals(0, results.size());
			results = getResults("description:leonardo vinci");
			assertEquals(3, results.size());
			results = getResults("vinci -picasso");
			assertEquals(3, results.size());
			results = getResults("leonardo -picasso");
			assertEquals(3, results.size());
			results = getResults("leonardo -leonardo");
			assertEquals(0, results.size());
			results = getResults("picasso -leonardo");
			assertEquals(1, results.size());
		} catch (SolrServerException e) {
			fail(e.toString());
		}

	}

	@Test
	public void testScores() throws SolrServerException {

		System.out.println(explain("picasso"));
	}

	// @Test
	// public void testResults() {
	// try {
	// SolrDocumentList results = getResults("leonardo");
	// assertEquals(3, results.size());
	// // check if the status is not corrupted
	// results = getResults("leonardo");
	// assertEquals(3, results.size());
	// results = getResults("vinci");
	// assertEquals(3, results.size());
	// results = getResults("test");
	// assertEquals(1, results.size());
	// results = getResults("ThisTermIsNotInTheIndexMuahahah");
	// assertEquals(0, results.size());
	// } catch (SolrServerException e) {
	// fail(e.toString());
	// }
	//
	// }

}
