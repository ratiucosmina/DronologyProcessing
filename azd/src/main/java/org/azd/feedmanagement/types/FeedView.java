package org.azd.feedmanagement.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * A view on top of a feed.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedView {
    /***
     * Id of the view.
     */
    @JsonProperty("id")
    private String id;
    /***
     * Name of the view.
     */
    @JsonProperty("name")
    private String name;
    /***
     * Url of the view.
     */
    @JsonProperty("url")
    private String url;
    /***
     * Type of view.
     */
    @JsonProperty("type")
    private String type;
    /***
     * Related REST links.
     */
    @JsonProperty("_links")
    private FeedReferenceLinks _links;
    /***
     * Visibility status of the view.
     */
    @JsonProperty("visibility")
    private String visibility;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FeedReferenceLinks get_links() {
        return _links;
    }

    public void set_links(FeedReferenceLinks _links) {
        this._links = _links;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "FeedView{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", visibility='" + visibility + '\'' +
                '}';
    }
}
