package com.demo.flowable.dto;

import org.flowable.engine.repository.ProcessDefinition;

/**
 * @author zhangjuwa
 * @date 2019/5/22
 * @since jdk1.8
 **/
public class ProcessDefinitionOutput implements ProcessDefinition {

    private String id;
    private String category;
    private String name;
    private String key;
    private String description;
    private Integer version;
    private String resourceName;
    private String deploymentId;
    private String diagramResourceName;
    private String tenantId;
    private Boolean startFormKey;
    private Boolean graphicalNotation;
    private Boolean suspended;
    private String derivedFrom;
    private String derivedFromRoot;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public String getResourceName() {
        return this.resourceName;
    }

    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }

    @Override
    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }

    @Override
    public boolean hasStartFormKey() {
        return this.startFormKey;
    }

    @Override
    public boolean hasGraphicalNotation() {
        return this.graphicalNotation;
    }

    @Override
    public boolean isSuspended() {
        return this.suspended;
    }

    @Override
    public String getTenantId() {
        return this.tenantId;
    }

    @Override
    public String getDerivedFrom() {
        return this.derivedFrom;
    }

    @Override
    public String getDerivedFromRoot() {
        return this.derivedFromRoot;
    }

    @Override
    public int getDerivedVersion() {
        return 0;
    }

    @Override
    public String getEngineVersion() {
        return null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getStartFormKey() {
        return startFormKey;
    }

    public void setStartFormKey(Boolean startFormKey) {
        this.startFormKey = startFormKey;
    }

    public Boolean getGraphicalNotation() {
        return graphicalNotation;
    }

    public void setGraphicalNotation(Boolean graphicalNotation) {
        this.graphicalNotation = graphicalNotation;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public void setDerivedFrom(String derivedFrom) {
        this.derivedFrom = derivedFrom;
    }

    public void setDerivedFromRoot(String derivedFromRoot) {
        this.derivedFromRoot = derivedFromRoot;
    }
}
