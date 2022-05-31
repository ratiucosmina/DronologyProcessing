import com.atlassian.jira.rest.client.api.domain.Issue;
import org.azd.enums.WorkItemOperation;
import org.azd.exceptions.AzDException;
import org.azd.utils.AzDClientApi;
import org.azd.workitemtracking.WorkItemTrackingApi;
import org.azd.workitemtracking.types.WorkItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JiraToAzureTranslator {
    AzDClientApi azureApi;
    WorkItemTrackingApi witApi;
    public Map<String, String> mapping = new HashMap<>();

    public JiraToAzureTranslator(String organization, String project, String personalAccessToken) {
        this.azureApi = new AzDClientApi(organization, project, personalAccessToken);
        this.witApi = azureApi.getWorkItemTrackingApi();
    }

    public void translateArtifacts(List<Optional<Issue>> issues){
        for (Optional<Issue> optionalIssue : issues)
            if (optionalIssue.isPresent()) {
                try {
                    createWorkItem(optionalIssue.get());
                } catch (AzDException e) {
                    e.printStackTrace();
                }
            }
    }

    private void createWorkItem(Issue issue) throws AzDException {
        String azureType = getAzureType(issue.getIssueType().getName());
//        WorkItem item = witApi.createWorkItem(azureType, WorkItemOperation.ADD, issue.getSummary(), issue.getDescription(), issue.getLabels().toArray(new String[0]));
        mapping.put(issue.getKey(), issue.getIssueType().getName());
    }

    public String getAzureType(String jiraType) {
        switch (jiraType) {
            case "Design Definition":
                return "Functional Specification";
            case "Sub-task":
                return "Task";
            case "Process:":
                return "Requirement";
            case "Hazard":
            case "Fault":
                return "Risk";
            case "Context":
            case "Environmental Assumption":
            case "Rationale":
                return "Documentation";
            case "Acceptance Test":
            case "Simulation":
                return "Test Case";
            case "Formal Review":
            case "Safety Analysis":
                return "Review";
            default:
                return jiraType;

        }
    }

}
