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

package eu.europeana.querylog.learn.measure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.querylog.QueryAssessment;
import eu.europeana.querylog.QueryAssessment.RelevantDocument;

/**
 * Computes the Normalized Discounted Comulative Gain (NDCG). Use Binary
 * Relevance (rel or notrel)
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Mar 11, 2014
 */
public class BNdcg extends AbstractMeasure implements Measure {

	public static final String NAME = "bNDCG";

	public BNdcg() {
		name = NAME;
	}

	@Override
	public double match(List<String> results, QueryAssessment assessment) {
		double idcg = idcg(results, assessment);
		// this means that in the assessment there are no relevant results.
		if (idcg == 0.0)
			return 0.0;
		return dcg(results, assessment) / idcg(results, assessment);
	}

	/**
	 * computes the discounted cumulative gain.
	 */
	private double dcg(List<String> results, QueryAssessment assessment) {
		assessment.sort();
		Map<String, RelevantDocument> relevance = new HashMap<String, RelevantDocument>();
		for (RelevantDocument dr : assessment.getAssessment()) {
			relevance.put(dr.getUri(), dr);
		}
		double dcg = 0d;
		int k = 1;
		for (String uri : results) {
			RelevantDocument dr = relevance.get(uri);
			if (dr != null && dr.getRelevance() > 0) {

				dcg += 1 / (Math.log(k + 1) / Math.log(2));
			}
			k++;
		}
		return dcg;
	}

	/**
	 * computes the ideal discounted cumulative gain.
	 */
	private double idcg(List<String> results, QueryAssessment assessment) {
		assessment.sort();
		double idcg = 0d;
		int k = 1;
		for (RelevantDocument dr : assessment.getAssessment()) {
			if (dr.getRelevance() > 0) {
				idcg += 1 / (Math.log(k + 1) / Math.log(2));
			}
			k++;
		}
		return idcg;
	}

}
