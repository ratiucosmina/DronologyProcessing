package org.azd.build.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.azd.core.types.Project;

/***
 * Represents a reference to a definition.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefinitionReference {
    /***
     * The date this version of the definition was created.
     */
    @JsonProperty("createdDate")
    private String createdDate;
    /***
     * The ID of the referenced definition.
     */
    @JsonProperty("id")
    private String id;
    /***
     * The name of the referenced definition.
     */
    @JsonProperty("name")
    private String name;
    /***
     * The REST URL of the definition.
     */
    @JsonProperty("url")
    private String url;
    /***
     * The definition's URI.
     */
    @JsonProperty("uri")
    private String uri;
    /***
     * The folder path of the definition.
     */
    @JsonProperty("path")
    private String path;
    /***
     * The type of the definition.
     */
    @JsonProperty("type")
    private String type;
    /***
     * A value that indicates whether builds can be queued against this definition.
     */
    @JsonProperty("queueStatus")
    private String queueStatus;
    /***
     * The definition revision number.
     */
    @JsonProperty("revision")
    private int revision;
    /***
     * A reference to the project.
     */
    @JsonProperty("project")
    private Project project;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(String queueStatus) {
        this.queueStatus = queueStatus;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "DefinitionReference{" +
                "createdDate='" + createdDate + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", uri='" + uri + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", queueStatus='" + queueStatus + '\'' +
                ", revision=" + revision +
                ", project=" + project +
                '}';
    }
}
