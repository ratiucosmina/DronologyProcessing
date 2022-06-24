package org.azd.git.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/***
 * List of repositories
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repositories {
    /***
     * List of repositories
     */
    @JsonProperty("value")
    private List<Repository> value;

    public List<Repository> getRepositories() {
        return value;
    }

    public void setRepositories(List<Repository> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Repositories{" +
                "value=" + value +
                '}';
    }
}
