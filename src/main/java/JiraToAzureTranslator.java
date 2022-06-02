import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.internal.json.IssueJsonParser;
import org.azd.enums.WorkItemOperation;
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
                createLinks(optionalIssue.get());
            }

    }

    private void createWorkItem(Issue issue) throws AzDException {
        String azureType = getAzureType(issue.getIssueType().getName());
        WorkItem item;
        String title = issue.getSummary()==null? issue.getKey():issue.getSummary();
        String description = issue.getDescription()==null? "": issue.getDescription();
        String[] labels = issue.getLabels()==null? new String[0]: issue.getLabels().toArray(new String[0]);
        item = witApi.createWorkItem(azureType, WorkItemOperation.ADD, title, description, labels);
        mapping.put(issue.getKey(), item);
    }

    private void createLinks(Issue issue) {
        WorkItem source = mapping.get(issue.getKey());
        if (source == null) {
            System.out.println(issue);
            return;
        }
        List<WorkItemRelations> links = source.getRelations();
        if (links == null)
            links = new ArrayList<>();

        IssueField parentField = issue.getField("parent");
        if (parentField != null && parentField.getValue() != null) {
            try {
                JSONObject value = (JSONObject) parentField.getValue();
                String parentKey = value.get("key").toString();
                if (!mapping.containsKey(parentKey)) {
                    System.out.println(parentKey);
                    System.out.println(issue.getIssueType().getName() + " no visible parent");
                } else {
                    WorkItemRelations rel = new WorkItemRelations();
                    rel.setRel("Child");
                    rel.setUrl(mapping.get(parentKey).getUrl());
                    links.add(rel);
                    System.out.println("YES " + issue.getIssueType().getName() + " "+ issue.getSummary()+ " has parent");
                }
            } catch (JSONException e) {
                System.out.println("Parent parsing failed");
            }
        }
        for (IssueLink link : issue.getIssueLinks()) {
            WorkItem target = mapping.getOrDefault(link.getTargetIssueKey(), null);
            if (target == null)
                continue;
            WorkItemRelations relations = new WorkItemRelations();
            relations.setUrl(target.getUrl());
            relations.setRel(getAzureRelationType(link.getIssueLinkType().getName()));
            links.add(relations);
        }
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
            case "Clones":
                return "Duplicates";
            case "Tests":
                return "Tests";
            default:
                return "Related";
        }
    }

}
