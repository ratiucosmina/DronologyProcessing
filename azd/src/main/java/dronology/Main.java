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

    public static void main(String[] args) {
//        JiraJsonParser parser = new JiraJsonParser();
//        JiraToAzureTranslator translator = new JiraToAzureTranslator("ratiucosmina", "Dronology-Final", "bywbcvqjncljzxizgibtvatchkwggtok2qzztyetmugyco5zrhoq");
//        UserStudyScript studyScript = new UserStudyScript("ratiucosmina", "shaznlxvygpx73f66thoixaxoq4rvq53es73x5f2axb24wyu7zsa");
        UserStudyScript studyScript = new UserStudyScript("christophmayr-dorn0649","qepf4l7d3azzty6rwgws6vhdf3bgkja25xqezxnphgdkm7uyiryq");
        try {
            studyScript.setUpProject("UserStudy",1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
