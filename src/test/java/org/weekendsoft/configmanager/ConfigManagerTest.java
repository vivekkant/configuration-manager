package org.weekendsoft.configmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConfigManagerTest {
	
	private static File tempdir = null;
	private static File propFile = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tempdir = new File(System.getProperty("java.io.tmpdir"));
		propFile = new File(tempdir, "testprop1.properties");
		PrintWriter out = new PrintWriter(new FileWriter(propFile));
		out.println("fprop2=value2");
		out.flush();
		out.close();
		
		System.setProperty("app.properties", propFile.getAbsolutePath());
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		propFile.deleteOnExit();
	}

	@Test
	void createInstanceTest() {
		ConfigManager cfg = ConfigManager.getInstance();
		assertNotNull(cfg);
	}
	
	@Test
	void addPropertyTest() {
		ConfigManager cfg = ConfigManager.getInstance();
		cfg.setConfigProperty("key1", "value1");

		String value = cfg.getProperty("key1");
		assertNotNull(value);
		assertEquals("value1", value);
		
		String value2 = cfg.getProperty("key2");
		assertNull(value2);
		
		String value3 = cfg.getProperty("keu2", "defaultValue");
		assertEquals("defaultValue", value3);
	}
	
	@Test
	void intPropertyTest() {
		ConfigManager config = ConfigManager.getInstance();
		config.setConfigProperty("int1", "1");
		config.setConfigProperty("int2", "str");
		
		int val1 = config.getIntProperty("int1", -1);
		assertEquals(1, val1);
		
		int val2 = config.getIntProperty("int2", -1);
		assertEquals(-1, val2);
	}
	
	@Test
	void booleanPropertyTest() {
		ConfigManager config = ConfigManager.getInstance();
		config.setConfigProperty("bool1", "true");
		config.setConfigProperty("bool2", "str");
		
		boolean val1 = config.getBooleanProperty("bool1", false);
		assertEquals(true, val1);
		
		boolean val2 = config.getBooleanProperty("bool2", true);
		assertEquals(true, val2);
	}
	
	void loadPropertyTest() throws Exception {
		ConfigManager config = ConfigManager.getInstance();
		config.setConfigProperty("fprop1", "oldval");
		config.setConfigProperty("fprop2", "oldval");
		
		File propFile = new File(tempdir, "testprop2.properties");
		PrintWriter out = new PrintWriter(new FileWriter(propFile));
		out.println("fprop1=value1");
		out.flush();
		out.close();
		
		FileInputStream fis = new FileInputStream(propFile);
		config.loadProperties(fis);
		fis.close();
		
		String val = config.getProperty("fprop1");
		assertEquals("value1", val);
		
		String val2 = config.getProperty("fprop2");
		assertEquals("oldval", val2);
		
		propFile.delete();
	}
	
	@Test
	void classpathLoadTest() throws Exception {
		ConfigManager config = ConfigManager.getInstance();
		String val = config.getProperty("appprop1");
		assertEquals("appvalue1", val);
	}
	
	@Test
	void systemPropertyTest() throws Exception {
		ConfigManager config = ConfigManager.getInstance();
		String val = config.getProperty("fprop2");
		assertEquals("value2", val);
	}
	


}
