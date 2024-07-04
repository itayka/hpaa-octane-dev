/*
 * Certain versions of software accessible here may contain branding from Hewlett-Packard Company (now HP Inc.) and Hewlett Packard Enterprise Company.
 * This software was acquired by Micro Focus on September 1, 2017, and is now offered by OpenText.
 * Any reference to the HP and Hewlett Packard Enterprise/HPE marks is historical in nature, and the HP and Hewlett Packard Enterprise/HPE marks are the property of their respective owners.
 * __________________________________________________________________
 * MIT License
 *
 * Copyright 2012-2024 Open Text
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors ("Open Text") are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ___________________________________________________________________
 */

package com.microfocus.application.automation.tools.octane.tests;

import com.hp.octane.integrations.OctaneSDK;
import com.microfocus.application.automation.tools.octane.configuration.SDKBasedLoggerProvider;
import com.microfocus.application.automation.tools.octane.tests.build.BuildHandlerUtils;
import com.microfocus.application.automation.tools.octane.tests.xml.TestResultXmlWriter;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.Run;
import jenkins.model.Jenkins;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;

/**
 * Jenkins events life cycle listener for processing test results on build completed
 */
@Extension
@SuppressWarnings({"squid:S2699", "squid:S3658", "squid:S2259", "squid:S1872"})
public class TestListener {
	private static Logger logger = SDKBasedLoggerProvider.getLogger(TestListener.class);

	public static final String TEST_RESULT_FILE = "mqmTests.xml";


	public boolean processBuild(Run run) {
		FilePath resultPath = new FilePath(new FilePath(run.getRootDir()), TEST_RESULT_FILE);
		TestResultXmlWriter resultWriter = new TestResultXmlWriter(resultPath, run);
		boolean success = true;
		boolean hasTests = false;
		String jenkinsRootUrl = Jenkins.get().getRootUrl();

		try {
			for (OctaneTestsExtension ext : OctaneTestsExtension.all()) {
				if (ext.supports(run)) {
					TestResultContainer testResultContainer = ext.getTestResults(run, jenkinsRootUrl);
					if (testResultContainer != null && testResultContainer.getIterator().hasNext()) {
						resultWriter.writeResults(testResultContainer);
						hasTests = true;
					}
				}
			}
		} catch (Throwable t) {
			success = false;
			logger.error("failed to process test results", t);
		} finally {
			try {
				resultWriter.close();

				// we don't push individual maven module results (although we create the file for future use)
				if (!"hudson.maven.MavenBuild".equals(run.getClass().getName())) {
					if (success && hasTests) {
						String projectFullName = BuildHandlerUtils.getJobCiId(run);
						String parents = BuildHandlerUtils.getRootJobCiIds(run);
						logger.info("enqueued build '" + projectFullName + " #" + run.getNumber() + "' for test result submission");
						if (projectFullName != null) {
							OctaneSDK.getClients().forEach(octaneClient ->
									octaneClient.getTestsService().enqueuePushTestsResult(projectFullName, String.valueOf(run.getNumber()), parents));
						}
					}
				}
			} catch (XMLStreamException xmlse) {
				success = false;
				logger.error("failed to finalize test results processing", xmlse);
			}
		}
		return success && hasTests;
	}
}
