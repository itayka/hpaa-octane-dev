import com.gargoylesoftware.htmlunit.Page;
import com.hp.octane.plugins.jenkins.actions.PluginActions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: gullery
 * Date: 07/01/15
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */

public class TestPluginActions {
	final private String projectName = "root-job";

	@Rule
	final public JenkinsRule rule = new JenkinsRule();

	@Test
	public void testPluginInfoClass() {
		PluginActions.PluginInfo pluginInfo = new PluginActions.PluginInfo();
		assertEquals(pluginInfo.getType(), "jenkins");
		assertEquals(pluginInfo.getVersion(), "1.0.0");
	}

	@Test
	public void testProjectsListClass() throws IOException {
		PluginActions.ProjectsList projectsList = new PluginActions.ProjectsList();
		assertEquals(projectsList.getJobs().getClass(), String[].class);
		assertEquals(projectsList.getJobs().length, 0);

		rule.createFreeStyleProject(projectName);
		assertEquals(projectsList.getJobs().length, 1);
		assertEquals(projectsList.getJobs()[0], projectName);
	}

	@Test
	public void testPluginActionsMethods() {
		PluginActions pluginActions = new PluginActions();
		assertEquals(pluginActions.getIconFileName(), null);
		assertEquals(pluginActions.getDisplayName(), null);
		assertEquals(pluginActions.getUrlName(), "octane");
	}

	@Test
	public void testPluginActions_REST_About() throws IOException, SAXException {
		JenkinsRule.WebClient client = rule.createWebClient();
		Page page = client.goTo("octane/about", "application/json");
		JSONObject body = new JSONObject(page.getWebResponse().getContentAsString());
		assertTrue(body.has("type"));
		assertEquals(body.getString("type"), "jenkins");
		assertTrue(body.has("version"));
		assertEquals(body.getString("version"), "1.0.0");
	}

	@Test
	public void testPluginActions_REST_Jobs() throws IOException, SAXException {
		JenkinsRule.WebClient client = rule.createWebClient();
		Page page;
		JSONObject body;
		JSONArray jobs;

		page = client.goTo("octane/jobs", "application/json");
		body = new JSONObject(page.getWebResponse().getContentAsString());
		assertTrue(body.has("jobs"));
		jobs = body.getJSONArray("jobs");
		assertEquals(jobs.length(), 0);

		rule.createFreeStyleProject(projectName);
		page = client.goTo("octane/jobs", "application/json");
		body = new JSONObject(page.getWebResponse().getContentAsString());
		assertTrue(body.has("jobs"));
		jobs = body.getJSONArray("jobs");
		assertEquals(jobs.length(), 1);
		assertEquals(jobs.get(0), projectName);
	}
}
