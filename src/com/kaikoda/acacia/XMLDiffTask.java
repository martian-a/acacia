/*
Copyright (C) 2014  Sheila Ellen Thomson

This program is free software: you can redistribute it and/or modify
it under the terms of the MIT License (MIT) as published by
the Open Source Initiative.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
MIT License for more details.

You should have received a copy of the MIT License
along with this program.  If not, see <http://opensource.org/licenses/MIT>
*/

package com.kaikoda.acacia;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.xml.sax.SAXException;

public class XMLDiffTask extends Task {
	
	protected String propertyName;
	protected String controlPath;
	protected String testPath;
	protected List<Difference> differences;
	protected Project project;  	
	protected Vector filesets = new Vector();
	
	public void setControl(String controlIn) {
		this.controlPath = controlIn;
	}
	
	public void setTest(String testIn) {
		this.testPath = testIn;
	}
	
	public void setProperty(String propertyNameIn) {
		this.propertyName = propertyNameIn;
	}
	
	private void setDifferences() throws SAXException, IOException {
		DetailedDiff detailedDiff = new DetailedDiff(new Diff(this.getXmlString(this.controlPath), this.getXmlString(this.testPath)));
		this.differences = (List<Difference>) detailedDiff.getAllDifferences();
	}
	
	protected Integer getTotalDifferences() {
		if (this.differences == null) {
			return null;
		} 
		return this.differences.size();		
	}
	
    protected void validate() {
        if (this.controlPath == null) throw new BuildException("control file not set");
        if (this.testPath == null) throw new BuildException("test file not set");
    }	
	
	public void execute() {
		
		this.project = getProject();
		this.validate();
			
		try {
			
			this.setDifferences();
			this.project.setNewProperty(this.propertyName, Integer.toString(this.differences.size()));
			if (this.differences.size() > 0) {				
				this.project.log(this.differences.get(0).toString(), Project.MSG_ERR);
			}
			
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}				
		
	}
	
	private String getXmlString(String pathIn) throws IOException {
		return FileUtils.readFileToString(new File(pathIn), "utf-8");
	}
}