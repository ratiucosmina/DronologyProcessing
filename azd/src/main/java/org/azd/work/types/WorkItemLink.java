package org.azd.work.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * A link between two work items.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemLink {
    /***
     * The type of link.
     */
    @JsonProperty("rel")
    private String rel;
    /***
     * The source work item.
     */
    @JsonProperty("source")
    private WorkItemReference source;
    /***
     * The target work item.
     */
    @JsonProperty("target")
    private WorkItemReference target;

    @Override
    public String toString() {
        return "WorkItemLink{" +
                "rel='" + rel + '\'' +
                ", source=" + source +
                ", target=" + target +
                '}';
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public WorkItemReference getSource() {
        return source;
    }

    public void setSource(WorkItemReference source) {
        this.source = source;
    }

    public WorkItemReference getTarget() {
        return target;
    }

    public void setTarget(WorkItemReference target) {
        this.target = target;
        }
}