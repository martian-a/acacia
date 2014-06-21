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

import static org.junit.Assert.assertEquals;

import org.apache.tools.ant.Project;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sheila Ellen Thomson
 */
public class XMLDiffTaskTest {
	
	
	/**
	 * An instance of the ANT task.
	 */
	XMLDiffTask task;
	
	/**
	 * Configure the test environment. 
	 */
	@Before
	public void setUp() {
		task = new XMLDiffTask();
	}
		
	/**
	 * Check that the name of the property to assign the result to is set correctly. 
	 */
    @Test
	public void testSetPropertyName() {				
		
		String propertyName = "totalDifferences";		
		task.setProperty(propertyName);

		assertEquals(propertyName, task.propertyName);
		
	}
    
	/**
	 * Check that the path to the test file is set correctly. 
	 */
    @Test
	public void testSetTest() {				
		
    	String testPath = XMLDiffTaskTest.class.getResource("/data/test/same.xml").getFile();	
		task.setTest(testPath);

		assertEquals(testPath, task.testPath);
		
	}
    
    /**
	 * Check that the path to the control file is set correctly. 
	 */
    @Test
	public void testSetControl() {				
		
    	String controlPath = XMLDiffTaskTest.class.getResource("/data/control/same.xml").getFile();	
		task.setControl(controlPath);

		assertEquals(controlPath, task.controlPath);
		
	}
    
    
	/**
	 * Check that there are no differences.
	 */
	@Test
	public void testExecute_same() {
		
		String propertyName = "totalDifferences";
		String controlPath = XMLDiffTaskTest.class.getResource("/data/control/same.xml").getFile();
		String testPath = XMLDiffTaskTest.class.getResource("/data/test/same.xml").getFile();		
		
		task.setProperty(propertyName);
		task.setControl(controlPath);		
		task.setTest(testPath);
		task.setProject(new Project());
				
		task.execute();
		
		assertEquals(0, (int) task.getTotalDifferences());
		
	}
	
	/**
	 * Check that there are differences and that there are the expected number of differences.
	 */
	@Test
	public void testExecute_different() {
		
		String propertyName = "totalDifferences";
		String controlPath = XMLDiffTaskTest.class.getResource("/data/control/different.xml").getFile();
		String testPath = XMLDiffTaskTest.class.getResource("/data/test/same.xml").getFile();		
		
		task.setProperty(propertyName);
		task.setControl(controlPath);		
		task.setTest(testPath);
		task.setProject(new Project());
				
		task.execute();
		
		assertEquals(4, (int) task.getTotalDifferences());
	}
	
	/**
	 * Check that the description of the first error is as expected.
	 */
	@Test
	public void testExecute_different_firstError() {
		
		String propertyName = "totalDifferences";
		String controlPath = XMLDiffTaskTest.class.getResource("/data/control/different.xml").getFile();
		String testPath = XMLDiffTaskTest.class.getResource("/data/test/same.xml").getFile();		
		
		task.setProperty(propertyName);
		task.setControl(controlPath);		
		task.setTest(testPath);
		task.setProject(new Project());
				
		task.execute();
		
		assertEquals("Expected number of child nodes '5' but was '3' - comparing <root...> at /root[1] to <root...> at /root[1]", task.differences.get(0));
	}
	
}