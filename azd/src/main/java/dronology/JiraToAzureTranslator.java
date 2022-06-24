package dronology;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.IssueLinkType;
import org.azd.exceptions.AzDException;
import org.azd.utils.AzDClientApi;
import org.azd.workitemtracking.WorkItemTrackingApi;
import org.azd.workitemtracking.types.WorkItem;
import org.azd.workitemtracking.types.WorkItemRelations;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;

public class JiraToAzureTranslator {
    AzDClientApi azureApi;
    WorkItemTrackingApi witApi;
    public Map<String, WorkItem> mapping = new HashMap<>();

    public JiraToAzureTranslator(String organization, String project, String personalAccessToken) {
        this.azureApi = new AzDClientApi(organization, project, personalAccessToken);
        this.witApi = azureApi.getWorkItemTrackingApi();
    }

    public void translateArtifacts(List<Optional<Issue>> issues) {
        for (Optional<Issue> optionalIssue : issues)
            if (optionalIssue.isPresent()) {
                try {
                    createWorkItem(optionalIssue.get());
                } catch (AzDException e) {
                    e.printStackTrace();
                }
            }
        for (Optional<Issue> optionalIssue : issues)
            if (optionalIssue.isPresent()) {
                try {
                    createLinks(optionalIssue.get());
                } catch (AzDException e) {
                    e.printStackTrace();
                }
            }

    }

    private void createWorkItem(Issue issue) throws AzDException {
        String azureType = getAzureType(issue.getIssueType().getName());
        WorkItem item;
        String title = issue.getSummary() == null ? issue.getKey() : issue.getSummary();
        String description = issue.getDescription() == null ? "" : issue.getDescription();
        Map<String, Object> additionalFields = new HashMap<>();
        String[] labels = issue.getLabels() == null ? new String[0] : issue.getLabels().toArray(new String[0]);
        additionalFields.put("System.Tags", String.join(",", labels));
        if (azureType.equals("Risk"))
            additionalFields.put("probability", "0");
        if (azureType.equals("Review"))
            additionalFields.put("purpose", description);
        item = witApi.createWorkItem(azureType, title, description, additionalFields);
        mapping.put(issue.getKey(), item);
    }

    private void createLinks(Issue issue) throws AzDException {
        WorkItem source = mapping.get(issue.getKey());
        if (source == null) {
            System.out.println(issue);
            return;
        }
        Map<String, String> links = new HashMap<>();

        IssueField parentField = issue.getField("parent");
        if (parentField != null && parentField.getValue() != null) {
            try {
                JSONObject value = (JSONObject) parentField.getValue();
                String parentKey = value.get("key").toString();
                if (!mapping.containsKey(parentKey)) {
                    System.out.println(parentKey);
                    System.out.println(issue.getIssueType().getName() + " no visible parent");
                } else {
                    links.put("System.LinkTypes.Dependency-Forward", mapping.get(parentKey).getUrl());
                    System.out.println("YES " + issue.getIssueType().getName() + " " + issue.getSummary() + " has parent");
                }
            } catch (JSONException e) {
                System.out.println("Parent parsing failed");
            }
        }
        for (IssueLink link : issue.getIssueLinks()) {
            if (link.getIssueLinkType().getDirection() != IssueLinkType.Direction.OUTBOUND)
                continue;
            WorkItem target = mapping.getOrDefault(link.getTargetIssueKey(), null);
            if (target == null)
                continue;
            links.put(getAzureRelationType(link.getIssueLinkType().getName()), target.getUrl());
        }

        if (!links.isEmpty())
            witApi.addLinks(source.getId(), links);
    }


    public String getAzureType(String jiraType) {
        switch (jiraType) {
            case "Design Definition":
//                return "Functional Specification";
                return "CR Issue FD";
            case "Sub-task":
                return "Task";
            case "Process-Requirement":
                return "Requirement";
            case "Hazard":
            case "Fault":
                return "Risk";
            case "Context":
            case "Environmental Assumption":
            case "Rationale":
//                return "Documentation";
                return "Epic";
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

    private String getAzureRelationType(String jiraLinkType) {
        switch (jiraLinkType) {
//            case "Clones":
//                return "System.LinkTypes.Duplicates";
//            case "Tests":
//                return "System.LinkTypes.Tests";
            default:
                return "System.LinkTypes.Related";
        }
    }

}
