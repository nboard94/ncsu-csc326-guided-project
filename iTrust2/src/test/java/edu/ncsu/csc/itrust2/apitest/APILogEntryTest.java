package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.personnel.PasswordChangeForm;
import edu.ncsu.csc.itrust2.forms.personnel.PersonnelForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for API functionality for interacting with log entries.
 *
 * @author Kai Presler-Marshall
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APILogEntryTest {

    private MockMvc               mvc;

    PasswordEncoder               pe = new BCryptPasswordEncoder();
    PasswordEncoder               pe2 = new BCryptPasswordEncoder();
    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests LogEntryAPI and APILOgEntryController
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patientPW", roles = { "USER", "ADMIN" } )
    public void testLogEntryAPI () throws Exception {
        // Ensure there is at least one log entry by viewing users
        mvc.perform( get( "/api/v1/users" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        mvc.perform( get( "/api/v1/logentries" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        final Long id = LogEntry.getLogEntries().get( 0 ).getId();
        // Test getting a specific log entry.
        mvc.perform( get( "/api/v1/logentries/" + id ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // Test getting a non-existent log entry
        mvc.perform( get( "/api/v1/logentries/-1" ) ).andExpect( status().isNotFound() );
        
        final UserForm patient = new UserForm( "patientPW", "123456", Role.ROLE_PATIENT, 1 );

        User user = new User( patient );
        user.save();

        user = User.getByName( "patientPW" ); // ensure they exist

        final PersonnelForm personnel = new PersonnelForm();
        personnel.setAddress1( "1 Test Street" );
        personnel.setAddress2( "Address Part 2" );
        personnel.setCity( "Prag" );
        personnel.setEmail( "csc326.201.1@gmail.com" );
        personnel.setFirstName( "Test" );
        personnel.setLastName( "HCP" );
        personnel.setPhone( "123-456-7890" );
        personnel.setSelf( user.getUsername() );
        personnel.setState( State.NC.toString() );
        personnel.setZip( "27514" );
        mvc.perform( post( "/api/v1/personnel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( personnel ) ) );

        assertTrue( pe.matches( "123456", user.getPassword() ) );
        changePassword( user, "123456", "654321" );
        user = User.getByName( "patientPW" ); // reload so changes are visible
        assertFalse( pe.matches( "123456", user.getPassword() ) );
        assertTrue( pe.matches( "654321", user.getPassword() ) );
        
        changePassword(user, "", "123456");
        assertTrue( pe.matches( "654321", user.getPassword() ) );
        mvc.perform( get( "/api/v1/logentriesbyuser/" + user) ).andExpect( status().isOk() )
        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        String sstring = "201812555523";
        String estring = "201812559523";
         mvc.perform( get( "/api/v1/userlogsbydate/" + sstring + "/" + estring) ).andExpect( status().isOk() )
        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        
         mvc.perform( get( "/api/v1/sortedlogsbyuser/") ).andExpect( status().isOk() )
         .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
         
         mvc.perform( get( "/api/v1/tenlogsbyuser/") ).andExpect( status().isOk() )
         .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        

    }
    
    private void changePassword ( final User user, final String password, final String newP ) throws Exception {
        final PasswordChangeForm form = new PasswordChangeForm();
        form.setCurrentPassword( password );
        form.setNewPassword( newP );
        form.setNewPassword2( newP );
        mvc.perform( post( "/api/v1/changePassword" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) );
    }

}
