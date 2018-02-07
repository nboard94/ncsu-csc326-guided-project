#Author: cznesbit

Feature: Alert Users by Email
	As a user
	I want to be alerted by email when there is a change to my password, an appointment, or my account
	So that I can stay informed and manage my healthcare appropriately

Scenario: Password change
	Given the user is using test@ncsu.edu as their account email
	When the user changes their account password
	Then an email informing the user of the password change is sent to test@ncsu.edu including the MID, but not the password

Scenario: Appointment change
	Given the user is using test@ncsu.edu as their account email
	And the user has an appointment scheduled
	When the receiving HCP changes the time/date of the appointment
	Then an email informing the user of the change in their appointment is sent to test@ncsu.edu

Scenario: Temporary lock-out
	Given the user is using test@ncsu.edu as their account email
	When the user attempts to log in with the correct username, but the incorrect password three times in a row
	Then an email informing the user of their temporary lock-out status is sent to test@ncsu.edu

Scenario: Permanent lock-out
	Given the user is using test@ncsu.edu as their account email
	When the user attempts to log in with the correct username, but the incorrect password three times in a row
	And the user does this two more times in a 24-hour period
	Then an email informing the user of their banned status is sent to test@ncsu.edu

Scenario: No email address
	Given the user does not have any email associated with their account
	When the user changes their account password
	Then the log reports an event with transaction code 1404