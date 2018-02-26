package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Unit tests for the User class.
 *
 * @author jshore
 *
 */
public class UserTest {

	/**
	 * Tests equals comparison of two user objects. Also verifies getters and
	 * setters of the used properties.
	 */
	@Test
	public void testEqualsAndProperties () {
		final User u1 = new User();
		final User u2 = new User();

		assertFalse( u1.equals( new Object() ) );
		assertFalse( u1.equals( null ) );
		assertTrue( u1.equals( u1 ) );

		u1.setEnabled( 1 );
		assertTrue( 1 == u1.getEnabled() );
		u2.setEnabled( 1 );

		u1.setPassword( "abcdefg" );
		assertEquals( "abcdefg", u1.getPassword() );
		u2.setPassword( "abcdefg" );

		u1.setRole( Role.valueOf( "ROLE_PATIENT" ) );
		assertEquals( Role.valueOf( "ROLE_PATIENT" ), u1.getRole() );
		u2.setRole( Role.valueOf( "ROLE_PATIENT" ) );

		u1.setUsername( "abcdefg" );
		assertEquals( "abcdefg", u1.getUsername() );
		u2.setUsername( "abcdefg" );

		assertTrue( u1.equals( u2 ) );

		//testing enabled == null and other.enabled != null
		u1.setEnabled(null);
		u2.setEnabled(1);
		assertFalse(u1.equals(u2));
		//testing enable != other.enabled
		u1.setEnabled(3);
		assertFalse(u1.equals(u2));
		//set u1 = u2
		u1.setEnabled(1);

		//testing password == null and other. password != null
		u1.setPassword(null);
		u2.setPassword("123456");
		assertFalse(u1.equals(u2));
		//testing password != other.password
		u1.setPassword("654321");
		assertFalse(u1.equals(u2));
		//set u1 = u2
		u1.setPassword("123456");


		//testing role != other.role
		u1.setRole(Role.ROLE_ADMIN);
		assertFalse(u1.equals(u2));
		//set u1 = u2
		u1.setRole(Role.ROLE_PATIENT);

		//testing username == null and other.username != null
		u1.setUsername(null);
		assertFalse(u1.equals(u2));
		//testing role != other.role
		u1.setUsername("lksdjf");
		assertFalse(u1.equals(u2));
		//set u1 = u2
		u1.setUsername("abcdef");


	}

}
