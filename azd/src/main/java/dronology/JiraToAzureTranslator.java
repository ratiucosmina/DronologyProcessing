package dronology;

import at.jku.isse.designspace.sdk.core.model.*;
import at.jku.isse.designspace.sdk.gui.swt.ConnectToWorkspaceDialog;
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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.*;

public class JiraToAzureTranslator {
    InstanceType issueType;
    WorkItemTrackingApi witApi;
    public Map<String, Instance> mapping = new HashMap<>();

    public JiraToAzureTranslator(String organization, String project, String personalAccessToken, List<Optional<Issue>> issues) {
        Display display = new Display();
        Shell shell = new Shell(display);

        ConnectToWorkspaceDialog dialog = new ConnectToWorkspaceDialog(shell, "Dronology", "1.0");
        dialog.create();

        if (dialog.open() == Window.OK) {
            Workspace workspace = dialog.getSelectedWorkspace();          //we always use this workspace
            User user = dialog.getSelectedUser();
            Folder instancesFolder = dialog.getSelectedInstancesFolder();

            createInstanceTypes(workspace, dialog.selectedTypesFolder);
            translateArtifacts(issues, workspace, instancesFolder);

            display.dispose();
        }
    }

    private void createInstanceTypes(Workspace workspace, Folder typesFolder) {
        issueType = workspace.createInstanceType("Issue", typesFolder);
        issueType.createPropertyType("name", Cardinality.SINGLE, String.class);
        issueType.createPropertyType("issueType", Cardinality.SINGLE, String.class);
        issueType.createPropertyType("description", Cardinality.SINGLE, String.class);
        issueType.createPropertyType("labels", Cardinality.SINGLE, String.class);
        issueType.createPropertyType("probability", Cardinality.SINGLE, String.class);
        issueType.createPropertyType("purpose", Cardinality.SINGLE, String.class);
        issueType.createPropertyType("relatedTo", Cardinality.SET, issueType);
        issueType.createOpposablePropertyType("successor", Cardinality.SET, issueType, "predecessor", Cardinality.SET);
    }

    public void translateArtifacts(List<Optional<Issue>> issues, Workspace workspace, Folder instancesFolder) {
        for (Optional<Issue> optionalIssue : issues)
            if (optionalIssue.isPresent()) {
                try {
                    createWorkItem(optionalIssue.get(), workspace, instancesFolder);
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

    private void createWorkItem(Issue issue, Workspace workspace, Folder instancesFolder) throws AzDException {
        String azureType = getAzureType(issue.getIssueType().getName());
        String title = issue.getSummary() == null ? issue.getKey() : issue.getSummary();
        String description = issue.getDescription() == null ? "" : issue.getDescription();
        Map<String, Object> additionalFields = new HashMap<>();
        String[] labels = issue.getLabels() == null ? new String[0] : issue.getLabels().toArray(new String[0]);
        Instance item = workspace.createInstance(title, issueType, instancesFolder);
        item.propertyAsSingle("description").set(description);
        item.propertyAsSingle("issueType").set(azureType);
        item.propertyAsSingle("labels").set(String.join(",", labels));
        if (azureType.equals("Risk"))
            item.propertyAsSingle("probability").set("0");
        if (azureType.equals("Review"))
            item.propertyAsSingle("purpose").set(description);
//      ToDo:  item = witApi.createWorkItem(azureType, title, description, additionalFields);
        
        mapping.put(issue.getKey(), item);
    }

    private void createLinks(Issue issue) throws AzDException {
        Instance source = mapping.get(issue.getKey());
        if (source == null) {
            System.out.println(issue);
            return;
        }

        IssueField parentField = issue.getField("parent");
        if (parentField != null && parentField.getValue() != null) {
            try {
                JSONObject value = (JSONObject) parentField.getValue();
                String parentKey = value.get("key").toString();
                if (!mapping.containsKey(parentKey)) {
                    System.out.println(parentKey);
                    System.out.println(issue.getIssueType().getName() + " no visible parent");
                } else {
                    source.propertyAsSet("successor").add(mapping.get(parentKey));
                }
            } catch (JSONException e) {
                System.out.println("Parent parsing failed");
            }
        }
        for (IssueLink link : issue.getIssueLinks()) {
            if (link.getIssueLinkType().getDirection() != IssueLinkType.Direction.OUTBOUND)
                continue;
            Instance target = mapping.getOrDefault(link.getTargetIssueKey(), null);
            if (target == null)
                continue;
            source.propertyAsSet("relatedTo").add(target);
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
//            case "Clones":
//                return "System.LinkTypes.Duplicates";
//            case "Tests":
//                return "System.LinkTypes.Tests";
            default:
                return "System.LinkTypes.Related";
        }
    }

}
