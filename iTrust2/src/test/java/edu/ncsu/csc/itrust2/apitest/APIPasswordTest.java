package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

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
import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPasswordTest {

    private MockMvc               mvc;
    PasswordEncoder               pe = new BCryptPasswordEncoder();

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    private void changePassword ( final User user, final String password, final String newP ) throws Exception {
        final PasswordChangeForm form = new PasswordChangeForm();
        form.setCurrentPassword( password );
        form.setNewPassword( newP );
        form.setNewPassword2( newP );
        mvc.perform( post( "/api/v1/changePassword" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) );
    }

    // Save auto-formatter wont let this be a javadoc comment
    // Create user. Starts with password 123456.
    // Changes to 654321.
    // Reset to 98765.
    // Delete user
    @WithMockUser ( username = "patientPW", roles = { "USER", "ADMIN" } )
    @Test
    public void testValidPasswordChanges () throws Exception {

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

        final Personnel p = Personnel.getByName( user );
        p.delete();
        user.delete();

    }

    /**
     * This tests that invalid api requests fail. Invalid passwords and
     * expiration testing handled in unit tests.
     *
     * @throws Exception
     */
    @WithMockUser ( username = "patientPW", roles = { "USER", "ADMIN" } )
    @Test
    public void testInvalidPasswordChanges () throws Exception {

        // test unknown user
        final String pw = "123456";
        final String newP = "654321";
        final PasswordChangeForm form = new PasswordChangeForm();
        form.setCurrentPassword( pw );
        form.setNewPassword( newP );
        form.setNewPassword2( newP );
        mvc.perform( post( "/api/v1/changePassword" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isBadRequest() );

        mvc.perform( post( "/api/v1/requestPasswordReset" ).contentType( MediaType.APPLICATION_JSON )
                .content( "patientPW" ) ).andExpect( status().isBadRequest() );

    }
    /*
     * Credit for checking email:
     * https://www.tutorialspoint.com/javamail_api/javamail_api_checking_emails.
     * htm
     */
//    private PasswordResetToken getTokenFromEmail () {
//        final String username = "csc326s182034@gmail.com";
//        final String password = "csc326s182034@gmail.com";
//        final String host = "smtp.gmail.com";
//        PasswordResetToken token = null;
//        try {
//            // create properties field
//            final Properties properties = new Properties();
//            properties.put( "mail.store.protocol", "pop3" );
//            properties.put( "mail.pop3.host", host );
//            properties.put( "mail.pop3.port", "995" );
//            properties.put( "mail.pop3.starttls.enable", "true" );
//            final Session emailSession = Session.getDefaultInstance( properties );
//            // emailSession.setDebug(true);
//
//            // create the POP3 store object and connect with the pop server
//            final Store store = emailSession.getStore( "pop3s" );
//
//            store.connect( host, username, password );
//
//            // create the folder object and open it
//            final Folder emailFolder = store.getFolder( "INBOX" );
//            emailFolder.open( Folder.READ_WRITE );
//
//            // retrieve the messages from the folder in an array and print it
//            final Message[] messages = emailFolder.getMessages();
//            Arrays.sort( messages, ( x, y ) -> {
//                try {
//                    return y.getSentDate().compareTo( x.getSentDate() );
//                }
//                catch ( final MessagingException e ) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                return 0;
//            } );
//            for ( final Message message : messages ) {
//                // SUBJECT
//                if ( message.getSubject() != null && message.getSubject().contains( "iTrust2 Password Reset" ) ) {
//                    String content = (String) message.getContent();
//                    content = content.replaceAll( "\r", "" ); // Windows
//                    content = content.substring( content.indexOf( "?tkid=" ) );
//
//                    final Scanner scan = new Scanner( content.substring( 6, content.indexOf( '\n' ) ) );
//                    System.err.println( "token(" + content.substring( 6, content.indexOf( '\n' ) ) + ")end" );
//                    final long tokenId = scan.nextLong();
//                    scan.close();
//
//                    content = content.substring( content.indexOf( "temporary password: " ) );
//                    content = content.substring( 20, content.indexOf( "\n" ) );
//                    content.trim();
//
//                    if ( content.endsWith( "\n" ) ) {
//                        content = content.substring( content.length() - 1 );
//                    }
//
//                    token = new PasswordResetToken();
//                    token.setId( tokenId );
//                    token.setTempPasswordPlaintext( content );
//                    break;
//                }
//            }
//
//            // close the store and folder objects
//            emailFolder.close( false );
//            store.close();
//            return token;
//        }
//        catch ( final Exception e ) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
    @WithMockUser ( username = "patientPW", roles = { "USER", "ADMIN" } )
    @Test
    public void testValidRequestReset () throws Exception {

        final UserForm patient = new UserForm( "patientPW", "123456", Role.ROLE_PATIENT, 1 );

        User user = new User( patient );
        user.save();

        user = User.getByName( "patientPW" ); // ensure they exist
        
        final PersonnelForm personnel = new PersonnelForm();
        personnel.setAddress1( "1 Test Street" );
        personnel.setAddress2( "Address Part 2" );
        personnel.setCity( "Prag" );
        personnel.setEmail( "csc326s182034@gmail.com" );
        personnel.setFirstName( "Test" );
        personnel.setLastName( "HCP" );
        personnel.setPhone( "123-456-7890" );
        personnel.setSelf( user.getUsername() );
        personnel.setState( State.NC.toString() );
        personnel.setZip( "27514" );
        
        mvc.perform( post( "/api/v1/personnel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( personnel ) ) );
        assertTrue( pe.matches( "123456", user.getPassword() ) );
        mvc.perform( post( "/api/v1/requestPasswordReset" ).contentType( MediaType.APPLICATION_JSON )
                .content( user.getUsername() ) ).andExpect( status().isOk() );
//        final PasswordChangeForm pchange = new PasswordChangeForm();
//        PasswordResetToken tokenId = new PasswordResetToken();
//        tokenId = getTokenFromEmail();
       
//        mvc.perform( post( "/api/v1/resetPassword/?tkid=" + tokenId.getId() ).contentType( MediaType.APPLICATION_JSON )
//                .content( TestUtils.asJsonString( pchange ) ) ).andExpect( status().isOk() );
        final Personnel p = Personnel.getByName( user );
        p.delete();
        user.delete();
    }

}
