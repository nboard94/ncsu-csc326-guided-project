package edu.ncsu.csc.itrust2.controllers.api;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * REST controller for interacting with Log Entry-related endpoints This will
 * have somewhat reduced functionality compared to the other controllers because
 * we don't want users to be able to delete logged events (_even_ if they are
 * Personnel/an admin)
 *
 * @author Kai Presler-Marshall
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APILogEntryController extends APIController {

    /**
     * Retrieves and returns a List of all LogEntries in the system
     *
     * @return list of log entries
     */
    @GetMapping ( BASE_PATH + "/logentries" )
    public List<LogEntry> getLogEntries () {
        return LogEntry.getLogEntries();
    }

    /**
     * Retrieves and returns a List of all LogEntries where the given user is
     * primaryUser or secondaryUser
     *
     * @param user
     *            user for whom to retrieve logs
     *
     * @return list of log entries
     */
    @GetMapping ( BASE_PATH + "/logentriesbyuser/{user}" )
    public List<LogEntry> getLogEntriesForUser ( @PathVariable ( "user" ) final String user ) {
        return LogEntry.getAllForUser( user );
    }

    /**
     * Retrieves and returns a List of all LogEntries where the given user is
     * primaryUser or secondaryUser, sorted by most recent
     *
     * @param user
     *            user for whom to retrieve logs
     *
     * @return list of log entries
     */
    @GetMapping ( BASE_PATH + "/sortedlogsbyuser/{user}" )
    public List<LogEntry> getSortedForUser ( @PathVariable ( "user" ) final String user ) {
        final List<LogEntry> logs = LoggerUtil.getSortedForUser( user );
        for ( int i = 0; i < logs.size(); i++ ) {
            final LogEntry entry = logs.get( i );
            final Role primRole = User.getByName( entry.getPrimaryUser() ).getRole();
            Role secRole = null;
            if ( entry.getSecondaryUser() != null ) {
                secRole = User.getByName( entry.getSecondaryUser() ).getRole();
            }
            if ( primRole != Role.ROLE_PATIENT ) {
                if ( primRole == Role.ROLE_ADMIN ) {
                    entry.setMessage( "ROLE_ADMIN" );
                }
                else if ( primRole == Role.ROLE_HCP ) {
                    entry.setMessage( "ROLE_HCP" );
                }
                else {
                    entry.setMessage( "" );
                }
            }
            else if ( secRole != null && secRole != Role.ROLE_PATIENT ) {
                if ( secRole == Role.ROLE_ADMIN ) {
                    entry.setMessage( "ROLE_ADMIN" );
                }
                else if ( secRole == Role.ROLE_HCP ) {
                    entry.setMessage( "ROLE_HCP" );
                }
                else {
                    entry.setMessage( "" );
                }
            }
            else {
                entry.setMessage( "" );
            }
            logs.set( i, entry );
        }
        return logs;
    }

    /**
     * Retrieves and returns a List of the most recent ten LogEntries where the
     * given user is primaryUser or secondaryUser
     *
     * @param user
     *            user for whom to retrieve logs
     *
     * @return list of log entries
     */
    @GetMapping ( BASE_PATH + "/tenlogsbyuser/{user}" )
    public List<LogEntry> getTopTenForUser ( @PathVariable ( "user" ) final String user ) {
        final List<LogEntry> logs = LoggerUtil.getTopForUser( user, 10 );
        for ( int i = 0; i < logs.size(); i++ ) {
            final LogEntry entry = logs.get( i );
            final Role primRole = User.getByName( entry.getPrimaryUser() ).getRole();
            Role secRole = null;
            if ( entry.getSecondaryUser() != null ) {
                secRole = User.getByName( entry.getSecondaryUser() ).getRole();
            }
            if ( primRole != Role.ROLE_PATIENT ) {
                if ( primRole == Role.ROLE_ADMIN ) {
                    entry.setMessage( "ROLE_ADMIN" );
                }
                else if ( primRole == Role.ROLE_HCP ) {
                    entry.setMessage( "ROLE_HCP" );
                }
                else {
                    entry.setMessage( "" );
                }
            }
            else if ( secRole != null && secRole != Role.ROLE_PATIENT ) {
                if ( secRole == Role.ROLE_ADMIN ) {
                    entry.setMessage( "ROLE_ADMIN" );
                }
                else if ( secRole == Role.ROLE_HCP ) {
                    entry.setMessage( "ROLE_HCP" );
                }
                else {
                    entry.setMessage( "" );
                }
            }
            else {
                entry.setMessage( "" );
            }
            logs.set( i, entry );
        }
        return logs;
    }

    /**
     * Retrieves and returns a list of the user's logs within a specified date
     * range.
     *
     * @param user
     *            user for whom to retrieve logs
     *
     * @param start
     *            start date of logs to retrieve
     * @param end
     *            end date of logs to retrieve
     *
     * @return list of log entries
     */
    @GetMapping ( BASE_PATH + "/userlogsbydate/{user}/{syear}/{smonth}/{sday}/{eyear}/{emonth}/{eday}" )
    public List<LogEntry> getUserLogsByDate ( @PathVariable ( "user" ) final String user,
            @PathVariable ( "syear" ) final int syear, @PathVariable ( "smonth" ) final int smonth,
            @PathVariable ( "sday" ) final int sday, @PathVariable ( "eyear" ) final int eyear,
            @PathVariable ( "emonth" ) final int emonth, @PathVariable ( "eday" ) final int eday ) {
        final Calendar s = Calendar.getInstance();
        final Calendar e = Calendar.getInstance();
        s.set( syear, smonth, sday, 0, 0, 0 );
        e.set( eyear, emonth, eday, 0, 0, 0 );
        final Date start = s.getTime();
        final Date end = e.getTime();
        final List<LogEntry> logs = LogEntry.getByDateForUser( user, start, end );
        for ( int i = 0; i < logs.size(); i++ ) {
            final LogEntry entry = logs.get( i );
            final Role primRole = User.getByName( entry.getPrimaryUser() ).getRole();
            Role secRole = null;
            if ( entry.getSecondaryUser() != null ) {
                secRole = User.getByName( entry.getSecondaryUser() ).getRole();
            }
            if ( primRole != Role.ROLE_PATIENT ) {
                if ( primRole == Role.ROLE_ADMIN ) {
                    entry.setMessage( "ROLE_ADMIN" );
                }
                else if ( primRole == Role.ROLE_HCP ) {
                    entry.setMessage( "ROLE_HCP" );
                }
                else {
                    entry.setMessage( "" );
                }
            }
            else if ( secRole != null && secRole != Role.ROLE_PATIENT ) {
                if ( secRole == Role.ROLE_ADMIN ) {
                    entry.setMessage( "ROLE_ADMIN" );
                }
                else if ( secRole == Role.ROLE_HCP ) {
                    entry.setMessage( "ROLE_HCP" );
                }
                else {
                    entry.setMessage( "" );
                }
            }
            else {
                entry.setMessage( "" );
            }
            logs.set( i, entry );
        }
        return logs;
    }

    /**
     * Retrieves and returns a specific log entry specified by the id provided.
     *
     * @param id
     *            The id of the log entry, as generated by Hibernate and used as
     *            the primary key
     * @return response
     */
    @GetMapping ( BASE_PATH + "/logentries/{id}" )
    public ResponseEntity getEntry ( @PathVariable ( "id" ) final Long id ) {
        final LogEntry entry = LogEntry.getById( id );
        return null == entry
                ? new ResponseEntity( errorResponse( "No log entry found for id " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( entry, HttpStatus.OK );
    }

}
