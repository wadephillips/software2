# Acme Calendar

## Evaluation Access Credentials
**Username**

test

**Password**

test

## Notes by Requirement
### A
The login form displays login information and error messages in English and Spanish.

### C
Lambda expressions were used to assist in the display of the schedule.  They are in calendar.components.CalendarPane
which handles the display of the calendar.  There are lambda functions in showCalendarByWeek(), showCalendarByMonth(),
 addAppointment(), editAppointemnt(), and
compareAndInsertInTable()
### E
The calendar automatically adjusts appointment display times to match the logged in user's timezone.

### F - Exception Controls
- Prevent scheduling an appointment outside of business hours - This is handled in the calendar.components
.CalendarPane.isValid() method.  This method throws an exception if any of a number of conditions are not met
when an appointment is created or edited.  Exceptions are caught and displayed to the user as an alert.
- Prevent scheduling overlapping appointments - This is handled by the calendar.components.CalendarPane
.checkForOverlappingAppointment() method.  This method throws an exception if any of a number of validation conditions are not
met when an appointment is created or edited.  Exceptions are caught and displayed to the user as an alert.
- Prevent entering non-existent or or invalid customer data - This is handled by the calendar.controllers
.CustomerEditFormController in the methods validateCustomer() and validateAddress().  Each of these methods checks a
number of conditions and will throw an Exception if validation fails.  This Exception is caught by the calling event
and an alert is displayed.
- Prevent entering an incorrect username and password -   This is handled by a try/catch block in the calendar.controllers.LoginController.submitLogin() method.  An unknown username or mismatched username and password combinations cause an Exception to be
thrown.  The Exception is caught and the Exceptions's message is displayed to the user as an error above the login
field.

### G
The calendar.Main.alert() and calendar.Main.popup() methods display standard alert and popup messages to the user.

### H
At the time of successful authentication the calendar.controllers.LoginController.submitLogin() method queries the user's upcomming appointments.  If any of the user's appointments have start times in the next 15 minutes a popup is displayed to notify them of an up comming appointment.

### J
The application tracks user activity by recording the user's username and a timestamp of the login.  This is handled in the calendar.controllers.LoginController.submitLogin() method via a call to the super.sendToLog() method.  This method writes a simple mesage to log file located just above the root of the project (project_root/../logs/logins.txt);

## Assumptions made about the Data Model
None of the provided tables contain auto-incementing primary keys.  Id assignment is handled by the calendar.models.Model.getNextId() method.  All Models that create brand new database records call this method prior to the INSERT query.

The assumption is that if a user created the appointment then it is their appointment.  So we match the createdBy field on the appointment with the logged in user.


## Other Notes

The **Acme Calendar** has four main sections; Login, Calendars, Customers, and Reports.  Each has its own button at the top of the application window.

## Login
The login scene loads first upon running the application.  The form automatically detects the users Locale and will present the form and login error messages in English or Spanish.

Incorrect combinations of username and password will result in an error message.

Upon authentication the logged in users information is stored as a static variable, Main.loggedInUser;

## Calendars
The Calendar section displays appointments in a TableView.  All appointments are inserted into the table view using lambda functions.

The Calendar will display appointments by week or by month.  It displays only the appointments for the logged in user.  The assumption is that if a user created the appointment then it is their appointment.  So we match the createdBy field on the appointment with the logged in user.

The application utilizes the calendar.models.Appointment class to represent all appointments and to handle persisting to/retrieving from the database.  Upon instantiation an object of this class converts the start and end times for the appointment to UTC. All start and end time in the database are assumed to be stored in UTC.  There are getter methods on the Appointment class that will retrieve the start or end time either as a ZonedDateTime UTC or as a ZonedDateTime in the user's timezone.

## Customers

The customers section of the application displays a TableView containing all _active_ customers when first loaded. _Inactive_ customers are not displayed.

There are two buttons above the Table View, one for adding a new customer and one to edit an existing customer.
The _addCustomerButton_ is always enabled.  However, the editCustomerButton is disabled until a customer row is clicked in the TableView.


## Reports
There are 3 reports available in the reports section.
Each report has its own button on the left hand side of the screen.  Clicking on the reports button will load the report.


