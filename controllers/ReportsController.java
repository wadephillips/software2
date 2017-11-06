package calendar.controllers;

import javafx.event.ActionEvent;

public class ReportsController extends MainController {

    public void loadAppTypeReport(ActionEvent actionEvent) {
        System.out.println("Loading the report!! Type");
    }

    public void loadScheduleByConsultantReport(ActionEvent actionEvent) {

        System.out.println("Loading the report!! Consultant");
    }

    public void loadTotalAppointmentsReport(ActionEvent actionEvent) {
        System.out.println("Loading the report!! Total");
    }
}
