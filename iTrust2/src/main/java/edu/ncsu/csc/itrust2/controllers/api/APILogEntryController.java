package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
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
        return LoggerUtil.getSortedForUser( user );
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
        return LoggerUtil.getTopForUser( user, 10 );
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
