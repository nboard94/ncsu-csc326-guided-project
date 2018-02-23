package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class BasicHealthMetricsTest {

	@SuppressWarnings("static-access")
	@Test
	public void test() {
		BasicHealthMetrics metric = new BasicHealthMetrics();
		metric.hashCode();
		assertTrue(metric.equals(metric));
		BasicHealthMetrics metric2 = null;
		assertFalse(metric.equals(metric2));
		metric2 = new BasicHealthMetrics();
		metric2.setDiastolic(2);
		assertFalse(metric.equals(metric2));
		assertFalse(metric2.equals(metric));
		metric.setDiastolic(2);
		User hcp = new User();
		hcp.setUsername("hcp");
		User hcp2 = null;
		metric.setHcp(hcp);
		metric2.setHcp(hcp2);
		assertFalse(metric.equals(metric2));
		assertFalse(metric2.equals(metric));
		hcp2 = new User();
		hcp2.setUsername("hcp");
		metric2.setHcp(hcp2);
		metric.setHdl(null);
		metric2.setHdl(5);
		assertFalse(metric.equals(metric2));
		
		metric.setHdl(2);
		assertFalse(metric2.equals(metric));
		metric.setHdl(5);
		metric.setHeadCircumference(null);
		metric2.setHeadCircumference((float) 5);
		assertFalse(metric.equals(metric2));
		metric.setHeadCircumference((float) 6);
		assertFalse(metric.equals(metric2));
		metric2.setHeadCircumference((float) 6);


		metric.setHeight(null);
		metric2.setHeight((float) 5);
		assertFalse(metric.equals(metric2));
		metric.setHeight((float) 6);
		assertFalse(metric.equals(metric2));
		metric2.setHeight((float) 6);

		metric.setLdl(null);
		metric2.setLdl(5);
		assertFalse(metric.equals(metric2));
		
		metric.setLdl(2);
		assertFalse(metric2.equals(metric));
		metric.setLdl(5);
	
		User patient = new User();
		patient.setUsername("patient");
		User patient2 = null;
		metric.setPatient(patient);
		metric2.setPatient(patient2);
		assertFalse(metric.equals(metric2));
		assertFalse(metric2.equals(metric));
		patient2 = new User();
		patient2.setUsername("patient");
		metric2.setPatient(patient2);
		
		metric.setSystolic(null);
		metric2.setSystolic(5);
		assertFalse(metric.equals(metric2));
		metric.setSystolic(2);
		assertFalse(metric2.equals(metric));
		metric.setSystolic(5);
		
		metric.setTri(null);
		metric2.setTri(500);
		assertFalse(metric.equals(metric2));
		metric.setTri(200);
		assertFalse(metric2.equals(metric));
		metric.setTri(500);
		
		metric.setWeight(null);
		metric2.setWeight((float) 5);
		assertFalse(metric.equals(metric2));
		metric.setWeight((float) 6);
		assertFalse(metric.equals(metric2));
		metric2.setWeight((float) 6);
		
		assertTrue(metric.equals(metric2));
		
		assertFalse(metric.getBasicHealthMetrics().isEmpty());
		assertFalse(metric.getBasicHealthMetricsForHCP(hcp.getUsername()).isEmpty());
		//assertTrue(metric.getBasicHealthMetricsForPatient(patient.getUsername()).isEmpty());
	//	assertTrue(metric.getBasicHealthMetricsForHCPAndPatient(hcp.getUsername(), patient.getUsername()).isEmpty());
		
		assertEquals(metric.getPatient().getUsername(), "patient");
		assertEquals(metric.getHcp().getUsername(), "hcp");
		metric.setDiastolic(null);

	}

}
