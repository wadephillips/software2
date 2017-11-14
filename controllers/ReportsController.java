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
import java.time.format.DateTimeFormatter;

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

    public void loadScheduleByConsultantReport(ActionEvent actionEvent) {
        this.bodyPane.getChildren().clear();
//        Label label = new Label("Loading the report!! Consultant");
//        this.bodyPane.getChildren().add(label);
        String sql = "SELECT * FROM appointment a " +
                "JOIN customer c " +
                "ON a.customerId = c.customerId " +
                "WHERE date_format(a.start, '%Y%m%d') >= date_format(now(), '%Y%m%d') ORDER BY a.createdBy, a.start;";

        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println("Loading the report!! Consultant");
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
