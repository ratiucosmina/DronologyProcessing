import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import org.codehaus.jettison.json.JSONException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        JiraJsonParser parser = new JiraJsonParser();
        JiraToAzureTranslator translator = new JiraToAzureTranslator("ratiucosmina", "Robotic%20Arm%20CMMI", "bywbcvqjncljzxizgibtvatchkwggtok2qzztyetmugyco5zrhoq");
        try {
            List<Optional<Issue>> issues = parser.loadIssues();
            translator.translateArtifacts(issues);
            Map<String, Integer> links = new HashMap<>();
            for (Optional<Issue> issue : issues) {
                Issue issue1 = issue.get();
                for (IssueLink issueLink : issue1.getIssueLinks()) {
                    String issueType = issue1.getIssueType().getName();
                    String targetKey = issueLink.getTargetIssueKey();
                    if(!translator.mapping.containsKey(targetKey)) {
//                        System.out.println(targetKey+issueLink.getIssueLinkType().getName());
//                        String link = " UNDEFINED TARGET: " + issueLink.getIssueLinkType().getName();
//                        if (links.containsKey(link))
//                            links.put(link, links.get(link) + 1);
//                        else
//                            links.put(link, 1);
//                        String link2 = " UNDEFINED TARGET";
//                        if (links.containsKey(link2))
//                            links.put(link2, links.get(link2) + 1);
//                        else
//                            links.put(link2, 1);
                        continue;
                    }
                    String targetType = translator.mapping.get(targetKey);
                    if( issueLink.getIssueLinkType().getDirection().name().equals("INBOUND"))
                        continue;
                    String link = issueType + " -> " + issueLink.getIssueLinkType().getName() +" -> "+targetType;
                    if (links.containsKey(link))
                        links.put(link, links.get(link) + 1);
                    else
                        links.put(link, 1);
                    //                    links.add(issueLink.getIssueLinkType().getName());
//                    links.add(issue1.getIssueType().getName()+" -> " +issueLink.getIssueLinkType().getName());
                }
            }
            links.entrySet().stream().sorted(Comparator.comparingInt(l -> l.getValue())).forEach(l -> System.out.println(l.getKey() + "\t\t#"+l.getValue()));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
