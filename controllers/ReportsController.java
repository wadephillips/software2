package calendar.controllers;

import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportsController extends MainController {

    public void loadAppTypeReport(ActionEvent actionEvent) {

        System.out.println("Loading the report!! Type");
        String sql = "SELECT COUNT(appointmentId) as count, description as type, month(start) as month, YEAR(start) as year FROM appointment " +
//                "WHERE date_format(start, '%Y%m') = date_format(now(), '%Y%m')" +
                "GROUP BY month(start), description ;";

        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("count") + " | " + resultSet.getString("type") + " | " + resultSet.getInt("month")  + "/" + resultSet.getInt("year"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loadScheduleByConsultantReport(ActionEvent actionEvent) {

        System.out.println("Loading the report!! Consultant");
    }

    public void loadTotalAppointmentsReport(ActionEvent actionEvent) {
        System.out.println("Loading the report!! Total");
    }
}
