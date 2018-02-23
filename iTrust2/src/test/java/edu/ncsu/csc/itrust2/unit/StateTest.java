/**
 * 
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.enums.State;

/**
 * @author Apeksha
 *
 */
public class StateTest {

	@Test
	public void test() {
		assertEquals(State.AK.getName(), "Alaska");
		assertFalse(State.AK.getInfo().equals(State.AL.getInfo()));
		assertEquals(State.AK.getAbbrev(), "AK");
		assertEquals(State.NC, State.parse("bleh"));
		assertEquals(State.AK, State.parse("AK"));
		assertEquals(State.AK, State.parse("Alaska"));
	}

}
