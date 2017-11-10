package calendar.controllers;

import calendar.components.AppointmentDialogPane;
import calendar.models.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class MonthlyCalendarController extends BaseCalendarController {


    public AnchorPane calendarNavBar;
    /**
     * Labels to hold the day of the month in the viewCalendarByMonth view.
     * Labels are in a Grid Pane that is 7 columns X 6 rows.
     * The first row contains day of the week labels, Sunday(0) thru Saturday(6)
     * The following five rows are for the weeks in the month.
     */
    @FXML
    public Label day0_5;
    @FXML
    public Label day0_1;
    @FXML
    public Label day1_1;
    @FXML
    public Label day2_1;
    @FXML
    public Label day3_1;
    @FXML
    public Label day4_1;
    @FXML
    public Label day5_1;
    @FXML
    public Label day6_1;
    @FXML
    public Label day6_2;
    @FXML
    public Label day5_2;
    @FXML
    public Label day4_2;
    @FXML
    public Label day3_2;
    @FXML
    public Label day0_2;
    @FXML
    public Label day1_2;
    @FXML
    public Label day2_2;
    @FXML
    public Label day0_3;
    @FXML
    public Label day1_3;
    @FXML
    public Label day2_3;
    @FXML
    public Label day3_3;
    @FXML
    public Label day4_3;
    @FXML
    public Label day5_3;
    @FXML
    public Label day6_3;
    @FXML
    public Label day6_4;
    @FXML
    public Label day5_4;
    @FXML
    public Label day4_4;
    @FXML
    public Label day3_4;
    @FXML
    public Label day2_4;
    @FXML
    public Label day1_4;
    @FXML
    public Label day0_4;
    @FXML
    public Label day6_5;
    @FXML
    public Label day5_5;
    @FXML
    public Label day4_5;
    @FXML
    public Label day3_5;
    @FXML
    public Label day2_5;
    @FXML
    public Label day1_5;
    @FXML
    public Label day6_6;
    @FXML
    public Label day5_6;
    @FXML
    public Label day4_6;
    @FXML
    public Label day3_6;
    @FXML
    public Label day2_6;
    @FXML
    public Label day1_6;
    @FXML
    public Label day0_6;

    /**
     * Fields for the calendar grid
     */
    @FXML
    public VBox box0_5;
    @FXML
    public VBox box0_1;
    @FXML
    public VBox box1_1;
    @FXML
    public VBox box2_1;
    @FXML
    public VBox box3_1;
    @FXML
    public VBox box4_1;
    @FXML
    public VBox box5_1;
    @FXML
    public VBox box6_1;
    @FXML
    public VBox box6_2;
    @FXML
    public VBox box5_2;
    @FXML
    public VBox box4_2;
    @FXML
    public VBox box3_2;
    @FXML
    public VBox box0_2;
    @FXML
    public VBox box1_2;
    @FXML
    public VBox box2_2;
    @FXML
    public VBox box0_3;
    @FXML
    public VBox box1_3;
    @FXML
    public VBox box2_3;
    @FXML
    public VBox box3_3;
    @FXML
    public VBox box4_3;
    @FXML
    public VBox box5_3;
    @FXML
    public VBox box6_3;
    @FXML
    public VBox box6_4;
    @FXML
    public VBox box5_4;
    @FXML
    public VBox box4_4;
    @FXML
    public VBox box3_4;
    @FXML
    public VBox box2_4;
    @FXML
    public VBox box1_4;
    @FXML
    public VBox box0_4;
    @FXML
    public VBox box6_5;
    @FXML
    public VBox box5_5;
    @FXML
    public VBox box4_5;
    @FXML
    public VBox box3_5;
    @FXML
    public VBox box2_5;
    @FXML
    public VBox box1_5;
    @FXML
    public VBox box6_6;
    @FXML
    public VBox box5_6;
    @FXML
    public VBox box4_6;
    @FXML
    public VBox box3_6;
    @FXML
    public VBox box2_6;
    @FXML
    public VBox box1_6;
    @FXML
    public VBox box0_6;


    private List<Label> week1;

    private List<Label> week2;
    private List<Label> week3;
    private List<Label> week4;
    private List<Label> week5;
    private List<Label> week6;

    private List<List<Label>> middleWeeks;

    @FXML
    public Button showByWeekButton;

    @FXML
    public Button showByMonthButton;

    @FXML
    public Button previousMonthButton;

    @FXML
    public Button nextMonthButton;

    @FXML
    public Label monthYearLabel;

    public MonthlyCalendarController() {
        super();

    }


    @FXML
    public void showPreviousMonth(ActionEvent actionEvent) {

        System.out.println("Display the previous month");
        LocalDate firstOfPreviousMonth = this.firstDayOfDisplayedMonth.minusMonths(1);
        this.displayMonthAndYear(firstOfPreviousMonth);
        this.firstDayOfDisplayedMonth = firstOfPreviousMonth;
        this.setGridDates();

    }

    @FXML
    public void showNextMonth(ActionEvent actionEvent) {
        System.out.println("display the next month");
        LocalDate firstOfNextMonth = this.firstDayOfDisplayedMonth.plusMonths(1);
        this.displayMonthAndYear(firstOfNextMonth);
        this.firstDayOfDisplayedMonth = firstOfNextMonth;
        this.setGridDates();
    }

    @FXML
    protected void setGridDates() {
        LocalDate date = LocalDate.now();
        setGridDates( this.firstDayOfDisplayedMonth.getMonthValue(), this.firstDayOfDisplayedMonth.getYear());
    }

    @FXML
    protected void setGridDates(int month, int year) {
        LocalDate firstDayOfMonthToBeDisplayed = LocalDate.of(year, month, 1);
        DayOfWeek firstWeekday = firstDayOfMonthToBeDisplayed.getDayOfWeek();
        int firstWeekDayAsInt = firstWeekday.getValue();
        int lastDayInMonth = firstDayOfMonthToBeDisplayed.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        int i = 1;

        for (int j = 0; j < 7 ; j++) {
            Label day = week1.get(j);
            if ( firstWeekDayAsInt == 7 || j>=firstWeekDayAsInt){
                day.setText(String.valueOf(i));
                day.setVisible(true);
                i++;
            } else {
                day.setText("-1");
                day.setVisible(false);
            }
        }

        for (List<Label> week: middleWeeks){
            for (Label day: week) {
                day.setText(String.valueOf(i));
                i++;
            }
        }
        i = labelFinalWeek(lastDayInMonth, i, week5);
        labelFinalWeek(lastDayInMonth, i, week6);



    }

    private int labelFinalWeek(int lastDayInMonth, int i, List<Label> week) {
        for (int j = 0; j < 7; j++) {
            Label day = week.get(j);
            if (i > lastDayInMonth){
                day.setText("-1");
                day.setVisible(false);
            } else {
                day.setText(String.valueOf(i));
                day.setVisible(true);
                i++;
            }
        }
        return i;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        setWeekArrays();
        setGridDates();
        displayMonthAndYear();
    }

    private void setWeekArrays() {
        week1 = new ArrayList<Label>(Arrays.asList(day0_1, day1_1, day2_1, day3_1, day4_1, day5_1, day6_1));
        week2 = new ArrayList<Label>(Arrays.asList(day0_2, day1_2, day2_2, day3_2, day4_2, day5_2, day6_2));
        week3 = new ArrayList<Label>(Arrays.asList(day0_3, day1_3, day2_3, day3_3, day4_3, day5_3, day6_3));
        week4 = new ArrayList<Label>(Arrays.asList(day0_4, day1_4, day2_4, day3_4, day4_4, day5_4, day6_4));
        week5 = new ArrayList<Label>(Arrays.asList(day0_5, day1_5, day2_5, day3_5, day4_5, day5_5, day6_5));
        week6 = new ArrayList<Label>(Arrays.asList(day0_6, day1_6, day2_6, day3_6, day4_6, day5_6, day6_6));

        middleWeeks = new ArrayList<>();
        Collections.addAll(middleWeeks, week2,week3,week4);

    }
    private void displayMonthAndYear() {
        displayMonthAndYear(this.firstDayOfDisplayedMonth);
    }

    private void displayMonthAndYear(LocalDate date) {
        String display = date.getMonth() + " " + date.getYear();
        display = display.toLowerCase();
        char[] chars = display.toUpperCase().toCharArray();
        char firstChar = chars[0];
        display = firstChar + display.substring(1);
        this.monthYearLabel.setText(display);
    }


    public void createAppointment(MouseEvent mouseEvent) {
        VBox box = (VBox) mouseEvent.getSource();
        String boxId = box.getId();
        int column = Integer.valueOf(boxId.substring(3,4));
        int row = Integer.valueOf((boxId.substring(5)));
        List<Node> children = box.getChildren();
        int dayOfMonth = -1;
        for (Node child: children) {
//            System.out.println(child);
            if (child instanceof Label){
                dayOfMonth = Integer.valueOf(((Label) child).getText());
            }
        }

        try {
            Dialog dialog = new Dialog();
            AppointmentDialogPane pane = new AppointmentDialogPane();
            dialog.setDialogPane(pane);
            

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, saveButtonType);

            
            Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    System.out.println("save button click registered" + dialogButton);
                    LocalDate date = pane.getDate();
                    //todo lambda for creating the appointment
                    return new Appointment();
                } else {
                    System.out.println("cancel");
                    return null;
                }
            });

//            System.out.println(dialog.showAndWait());
            Optional<Appointment> appointment = dialog.showAndWait();
            if (appointment.isPresent()){
                System.out.println("Insert appt into grid");
            } else {
                System.out.println("Nothing to do right now");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("row: " + row + " column: " + column + " day: " + dayOfMonth);
    }
}
