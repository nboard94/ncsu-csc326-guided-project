#Author: cznesbit

Feature: View Access Logs
	As a user
	I want to be able to view my access logs
	So that I can make sure no one but me is accessing my account

Scenario: Landing screen
	Given the user is a registered user of the iTrust2 Medical Records system
	And the user has authenticated themselves
	When the user logs in
	Then the user's landing screen displays the top ten most recent events including the following fields: name, role, date/time, transaction type

Scenario: Select date range
	Given the landing screen displays appropriately
	When the user selects 1/1/2000 as a start date
	And the user selects 1/1/2018 as an end date
	And the user submits
	Then the user's event viewer should display events from within this range in order from most recent to least

Scenario: Invalid start date
	Given the landing screen displays appropriately
	When the user selects 1/99/2018 as a start date
	And the user selects 12/1/2018 as an end date
	And the user submits
	Then an error message is displayed indicating an invalid start date
	And the event viewer contents remain unchanged

Scenario: Invalid end date
	Given the landing screen displays appropriately
	When the user selects 1/1/2018 as a start date
	And the user selects 12/99/2018 as an end date
	And the user submits
	Then an error message is displayed indicating an invalid end date
	And the event viewer contents remain unchanged

Scenario: End date before start date
	Given the landing screen displays appropriately
	When the user selects 12/1/2018 as a start date
	And the user selects 1/1/2018 as an end date
	And the user submits
	Then an error message is displayed indicating that the start date must precede the end date
	And the event viewer contents remain unchanged