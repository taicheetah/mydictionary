
package com.taicheetah.mydictionary.dto.oxfordAPI.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entries",
    "language",
    "lexicalCategory",
    "text"
})
@Generated("jsonschema2pojo")
public class LexicalEntry {

    @JsonProperty("entries")
    private List<Entry> entries = null;
    @JsonProperty("language")
    private String language;
    @JsonProperty("lexicalCategory")
    private LexicalCategory lexicalCategory;
    @JsonProperty("text")
    private String text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entries")
    public List<Entry> getEntries() {
        return entries;
    }

    @JsonProperty("entries")
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("lexicalCategory")
    public LexicalCategory getLexicalCategory() {
        return lexicalCategory;
    }

    @JsonProperty("lexicalCategory")
    public void setLexicalCategory(LexicalCategory lexicalCategory) {
        this.lexicalCategory = lexicalCategory;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
