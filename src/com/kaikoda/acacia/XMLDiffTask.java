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
	
	private String propertyName;
	private String controlPath;
	private String testPath;
	private List<Difference> differences;
	private Project project;  	
	private Vector filesets = new Vector();
	
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
		DetailedDiff diff = new DetailedDiff(new Diff(this.getXmlString(this.controlPath), this.getXmlString(this.testPath)));
		this.differences = diff.getAllDifferences();
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
				System.out.println(this.differences.get(0).toString());
			}
			
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}			
		
		
		
		
	}
	
	private String getXmlString(String pathIn) throws IOException {
		File xmlFile = new File(pathIn);
		String xmlString = FileUtils.readFileToString(xmlFile, "utf-8");
		return xmlString;
	}
}