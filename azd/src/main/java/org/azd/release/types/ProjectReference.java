package org.azd.release.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * Project reference
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectReference {
    /***
     * Project's unique identifier
     */
    @JsonProperty("id")
    private String id;
    /***
     * Project name
     */
    @JsonProperty("name")
    private String name;

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

    @Override
    public String toString() {
        return "ProjectReference{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
