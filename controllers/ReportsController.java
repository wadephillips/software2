package calendar.controllers;

import calendar.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ReportsController extends MainController {

    @FXML
    private StackPane bodyPane;

    public void loadAppTypeReport(ActionEvent actionEvent) {
        this.bodyPane.getChildren().clear();

        System.out.println("Loading the report!! Type");
        String sql = "SELECT COUNT(appointmentId) as count, description as type, month(start) as month, YEAR(start) as year FROM appointment " +
//                "WHERE date_format(start, '%Y%m') = date_format(now(), '%Y%m')" +
                "GROUP BY YEAR(start), MONTH(start), description " +
                "ORDER BY YEAR(start), MONTH(start);";

        ScrollPane scrollPane = new ScrollPane();

        VBox reportContainer = new VBox();
        reportContainer.setId("reportContainer");
        Label title = new Label("Count of Appointment Types by Month");
        title.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.5em;");
        reportContainer.getChildren().add(title);

        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){
            int month = 0;
            int year = 0;
            String monthYear = "";
            while (resultSet.next()) {

                int currentMonth = resultSet.getInt("month");
                int currentYear = resultSet.getInt("year");
                if (month != currentMonth || year != currentYear) {
                    System.out.println(month+year + " | " + currentMonth+currentYear);
                    month = currentMonth;
                    year = currentYear;
                    LocalDate  displayMonth = LocalDate.of(currentYear,currentMonth,1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", Main.getLocale());
                    Label monthLabel = new Label(formatter.format(displayMonth));
                    monthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1.25em;");
                    reportContainer.getChildren().add(monthLabel);
                }
                Label result = new Label(resultSet.getInt("count") + " | " + resultSet.getString("type"));
                reportContainer.getChildren().add(result);
//                System.out.println(resultSet.getInt("count") + " | " + resultSet.getString("type") + " | " + currentMonth + "/" + currentYear);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        scrollPane.setContent(reportContainer);
        this.bodyPane.getChildren().add(scrollPane);

    }

    //TODO: I don't think that this is working correctly.  It doesn't seem to be displaying any appts for the test user
    public void loadScheduleByConsultantReport(ActionEvent actionEvent) {
        this.bodyPane.getChildren().clear();
//        Label label = new Label("Loading the report!! Consultant");
//        this.bodyPane.getChildren().add(label);
        ScrollPane scrollPane = new ScrollPane();

        VBox reportContainer = new VBox();
        reportContainer.setId("reportContainer");
        Label title = new Label("Schedule by Consultant");
        title.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.5em;");
        reportContainer.getChildren().add(title);

        String sql = "SELECT * FROM appointment a " +
                "JOIN customer c " +
                "ON a.customerId = c.customerId " +
                "WHERE date_format(a.start, '%Y%m%d') >= date_format(now(), '%Y%m%d') ORDER BY a.createdBy, a.start;";
        String consultant = "";
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){

            while (resultSet.next()){
                String currentConsultant = resultSet.getString("createdBy");
                if (!consultant.equals(currentConsultant)) {
                    consultant = currentConsultant;
                    Label consultantLabel = new Label("Upcoming appointments for " + consultant);
                    consultantLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 1.25em;");
                    reportContainer.getChildren().add(consultantLabel);
                }
                ZonedDateTime start = ZonedDateTime.ofInstant(resultSet.getTimestamp("start").toInstant(), Main.getZone());
                ZonedDateTime end = ZonedDateTime.ofInstant(resultSet.getTimestamp("end").toInstant(), Main.getZone());

                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

                String appointmentString = formatter.format(start) + " - " + formatter.format(end) + " w/ " + resultSet.getString("customerName");

                Label appointmentLabel = new Label(appointmentString);
                reportContainer.getChildren().add(appointmentLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        scrollPane.setContent(reportContainer);
        this.bodyPane.getChildren().add(scrollPane);
        System.out.println("Loading the report!! Consultant");
    }

    public void loadTotalAppointmentsByConsultantReport(ActionEvent actionEvent) {
        this.bodyPane.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();

        VBox reportContainer = new VBox();
        reportContainer.setId("reportContainer");
        Label title = new Label("Total number of Appointments by Consultant");
        title.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.5em;");
        reportContainer.getChildren().add(title);

//        Label label = new Label("Loading the report!! Total");
//        this.bodyPane.getChildren().add(label);
//        System.out.println("Loading the report!! Total");
        String sql = "SELECT COUNT(appointmentId) as count, createdBy FROM appointment GROUP BY createdBy ORDER BY createdBy";

        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){

            while (resultSet.next()) {
                String display = resultSet.getString("createdBy") + " - " + Integer.toString(resultSet.getInt("count"));
                Label label = new Label(display);
                reportContainer.getChildren().add(label);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        scrollPane.setContent(reportContainer);
        this.bodyPane.getChildren().add(scrollPane);
    }
}
