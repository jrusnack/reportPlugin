/*
 * Copyright (C) 2012 Red Hat, Inc.     
 * 
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions of the 
 * GNU General Public License v.2.
 * 
 * Authors: Jan Rusnacko (jrusnack at redhat dot com)
 */

package com.redhat.engineering.jenkins.report.plugin.results;

import hudson.model.AbstractBuild;
import hudson.model.Api;
import hudson.model.ModelObject;
import java.io.Serializable;

/**
 *
 * @author Jan Rusnacko (jrusnack at redhat.com)
 */
public abstract class BaseResult implements ModelObject, Serializable{
   
    protected AbstractBuild<?, ?> owner;
    protected final String name;
    protected BaseResult parent;
    
	
   
    public BaseResult(String name) {
	this.name = name;
    }
    
    public String getName(){
	return name;
    }
   
    public BaseResult getParent() {
	return parent;
    }

    public void setParent(BaseResult parent) {
	this.parent = parent;
    }

    public AbstractBuild<?, ?> getOwner() {
	return owner;
    }

    public void setOwner(AbstractBuild<?, ?> owner) {
	this.owner = owner;
    }

    public String getDisplayName() {
	return getName();
    }

    public String getUrl() {
	return getName();
    }

    public Api getApi() {
	return new Api(this);
    }
    
}
