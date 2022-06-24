package org.azd.git.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.azd.common.types.Reference;
import org.azd.common.types.ReferenceLink;

/***
 * The class to represent a collection of REST reference links.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryReferenceLinks extends ReferenceLink {
    /***
     * Project reference url
     */
    @JsonProperty("project")
    private Reference project;
    /***
     * Repository url
     */
    @JsonProperty("web")
    private Reference web;
    /***
     * Repository SSH url
     */
    @JsonProperty("ssh")
    private Reference ssh;
    /***
     * REST url for commits
     */
    @JsonProperty("commits")
    private Reference commits;
    /***
     * REST url or branches
     */
    @JsonProperty("refs")
    private Reference refs;
    /***
     * REST url of pull requests
     */
    @JsonProperty("pullRequests")
    private Reference pullRequests;
    /***
     * Url of items
     */
    @JsonProperty("items")
    private Reference items;
    /***
     * REST url of pushes
     */
    @JsonProperty("pushes")
    private Reference pushes;

    public Reference getProject() {
        return project;
    }

    public void setProject(Reference project) {
        this.project = project;
    }

    public Reference getWeb() {
        return web;
    }

    public void setWeb(Reference web) {
        this.web = web;
    }

    public Reference getSsh() {
        return ssh;
    }

    public void setSsh(Reference ssh) {
        this.ssh = ssh;
    }

    public Reference getCommits() {
        return commits;
    }

    public void setCommits(Reference commits) {
        this.commits = commits;
    }

    public Reference getRefs() {
        return refs;
    }

    public void setRefs(Reference refs) {
        this.refs = refs;
    }

    public Reference getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(Reference pullRequests) {
        this.pullRequests = pullRequests;
    }

    public Reference getItems() {
        return items;
    }

    public void setItems(Reference items) {
        this.items = items;
    }

    public Reference getPushes() {
        return pushes;
    }

    public void setPushes(Reference pushes) {
        this.pushes = pushes;
    }

    @Override
    public String toString() {
        return "RepositoryReferenceLinks{" +
                "project=" + project +
                ", web=" + web +
                ", ssh=" + ssh +
                ", commits=" + commits +
                ", refs=" + refs +
                ", pullRequests=" + pullRequests +
                ", items=" + items +
                ", pushes=" + pushes +
                '}';
    }
}
