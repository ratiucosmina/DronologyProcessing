package user_study_script;

import org.azd.exceptions.AzDException;
import org.azd.utils.AzDClientApi;
import org.azd.workitemtracking.WorkItemTrackingApi;
import org.azd.workitemtracking.types.WorkItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class UserStudyScript {
    AzDClientApi azureApi;
    WorkItemTrackingApi witApi;
    Map<String, WorkItem> workItems = new HashMap<>();
    String organization;
    String personalAccessToken;

    public UserStudyScript(String organization, String personalAccessToken) {
        this.organization = organization;
        this.personalAccessToken = personalAccessToken;
    }

    public void setUpProject(String project) throws FileNotFoundException, AzDException {
        this.azureApi = new AzDClientApi(organization, project, personalAccessToken);
        this.witApi = azureApi.getWorkItemTrackingApi();

        BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\Cosmina\\Documents\\Work\\DesignSpace\\azure-devops-java-sdk\\azd\\src\\main\\java\\user_study_script\\userstudy1.txt"));
        file.lines()
                .map(line -> line.split(","))
                .forEach(line ->
                {
                    if (line.length>3) {
                        try {
                            createWorkItem(line[0], line[1], line[2], line[3]);
                        } catch (AzDException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            createLink(line[0], line[1], line[2]);
                        } catch (AzDException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void createWorkItem(String type, String name, String description, String state) throws AzDException {
        WorkItem item;

        Map<String, Object> additionalFields = new HashMap<>();
        item = witApi.createWorkItem(type, name, description, additionalFields);
        additionalFields.put("System.State", state);
        witApi.updateWorkItem(item.getId(),additionalFields);
        workItems.put(name, item);
    }

    private void createLink(String source, String target, String linkType) throws AzDException {
        Map<String, String> links = new HashMap<>();
        links.put(linkType, workItems.get(target).getUrl());
        witApi.addLinks(workItems.get(source).getId(), links);
    }

}
