package dronology;

import com.atlassian.jira.rest.client.api.domain.Issue;
import org.azd.enums.WorkItemExpand;
import org.azd.exceptions.AzDException;
import org.azd.workitemtracking.types.WorkItem;
import org.azd.workitemtracking.types.WorkItemRelations;
import org.codehaus.jettison.json.JSONException;
import user_study_script.UserStudyScript;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        JiraJsonParser parser = new JiraJsonParser();
        JiraToAzureTranslator translator = new JiraToAzureTranslator("ratiucosmina", "Dronology-Final", "bywbcvqjncljzxizgibtvatchkwggtok2qzztyetmugyco5zrhoq", parser.loadIssues());
//        UserStudyScript studyScript = new UserStudyScript("christophmayr-dorn0649","xrrz4rxg46p7xisrcmjh2hw63irp6dda4ezw4atjncv54kbr5i7q");
//        try {
//            studyScript.setUpProject("UserStudy1Prep");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
