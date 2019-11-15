package com.daprref.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DaprMessage {

    @JsonProperty("data")
    private AdditionOperands data;
    private String datacontenttype;
    private String specversion;
    private String id;
    private String source;
    private String type;

    public DaprMessage() {

    }

    public AdditionOperands getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(AdditionOperands data) {
        this.data = data;
    }

    public String getDatacontenttype() {
        return datacontenttype;
    }

    @JsonProperty("datacontenttype")
    public void setDatacontenttype(String datacontenttype) {
        this.datacontenttype = datacontenttype;
    }

    public String getSpecversion() {
        return specversion;
    }

    @JsonProperty("specversion")
    public void setSpecversion(String specversion) {
        this.specversion = specversion;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public DaprMessage(String id, String source, String type, String specversion, String datacontenttype, AdditionOperands data) {
        this.id = id;
        this.source = source;
        this.type = type;
        this.specversion = specversion;
        this.datacontenttype = datacontenttype;
        this.data = data;
    }
}