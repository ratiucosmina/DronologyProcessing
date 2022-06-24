package dronology;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.json.IssueJsonParser;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JiraJsonParser {

    private final String FOLDER_NAME = "demo";

    JSONArray issues = new JSONArray();

    public JiraJsonParser() {
        try {
            File file = new File("dronology_jira_big-BAK.json_BAK");
            InputStream is = new FileInputStream(file);
            String body = IOUtils.toString(is, StandardCharsets.UTF_8);
            JSONObject issueAsJson = new JSONObject(body);
            issues = issueAsJson.getJSONArray("issues");

        } catch (IOException | JSONException e) {
        }
    }

    /**
     * will search in every json file inside the demo folder
     */
    public List<Optional<Issue>> loadIssues() throws JSONException, IOException {

        List<Optional<Issue>> allIssues = new ArrayList<>();
        JSONObject jsonObj = null;
        for (int i = 0; i < issues.length(); i++) {
            JSONObject curIssue = issues.getJSONObject(i);
            allIssues.add(Optional.of(new IssueJsonParser(new JSONObject(), new JSONObject()).parse(curIssue)));
        }

        return allIssues;
    }
}
