# Acme Calendar
The **Acme Calendar** has four main sections; Login, Calendars, Customers, and Reports.  Each has its own button at the top of the application window.


## Login
The login scene loads first upon running the application.  The form automatically detects the users Locale and will present the form and login error messages in English or Spanish.

Incorrect combinations of username and password will result in an error message.

Upon authentication the logged in users information is stored as a static variable, Main.loggedInUser;

Other static variable on Main are:
- DATASOURCE - contains a package-private DataSource object which provides access to the database.  Connection information is stored in database.properties file.



## Calendars
The Calendar section displays appointments in a TableView.  All appointments are inserted into the table view with lambda functions.

The calendar will display appointments by week or by month.  It displays only the appointments for the logged in user.  The assumption is that if a user created the appointment then it is their appointment.  So we match the createdBy field on the appointment with the logged in user.

The application utilizes an Appointment class to represent all appointments and to handle persisting to/retrieving 
from the database.  Upon instantiation an object of this class converts the start and end times for the appointment 
to UTC. All start and end time in the database are assumed to be stored in UTC.  There are getter methods on the Appointment class that
 will retrieve the start or end time either as a ZonedDateTime UTC or as a ZonedDateTime in the users timezone.ds



## Customers 

The customers section of the application displays a TableView containing all _active_ customers when first loaded.

There are two buttons above the Table View, one for adding a new customer and one to edit an existing customer.
The _addCustomerButton_ is always enabled.  However, the editCustomerButton is disabled until a customer row is clicked in the TableView.


## Reports
There are 3 reports available in the reports section.
Each report has its own button on the left hand side of the screen.  Clicking on the reports button will load the report.


## Requirements
### A

### B
### C
### D
### E
### F Exception Controls
- Prevent scheduling an appointment outside of business hours - This is handled in the calendar.components
.CalendarPane.isValid() .  This method throws an exception if any of a number of conditions are not met 
when an appointment is created or edited.  Exceptions are caught and displayed to the user as an alert.
- Prevent scheduling overlapping appointments - This is handled by calendar.components.CalendarPane
.checkForOverlappingAppointment() .  This method throws an exception if any of a number of conditions are not met 
when an appointment is created or edited.  Exceptions are caught and displayed to the user as an alert.
- Prevent entering non-existent or or invalid customer data - 
- Prevent entering an incorrect username and password -   
### G
### H
### I
### J
