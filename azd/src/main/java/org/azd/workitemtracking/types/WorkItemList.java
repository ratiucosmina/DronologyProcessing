package org.azd.workitemtracking.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/***
 * List of work item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemList {
    /***
     * List of work item
     */
    @JsonProperty("value")
    private List<WorkItem> workItems;

    public List<WorkItem> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(List<WorkItem> workItems) {
        this.workItems = workItems;
    }

    @Override
    public String toString() {
        return "WorkItemList{" +
                "workItems=" + workItems +
                '}';
    }
}
