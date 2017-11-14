package calendar.controllers;

import calendar.models.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WeeklyCalendarController extends BaseCalendarController {

    public Button showByWeekButton;


    @FXML
    private Label weekYearLabel;

    @FXML
    private VBox sundayColumn;
    @FXML
    private Label sundayWeekDayLabel;
    @FXML
    private VBox mondayColumn;
    @FXML
    private Label mondayWeekDayLabel;
    @FXML
    private VBox tuesdayColumn;
    @FXML
    private Label tuesdayWeekDayLabel;
    @FXML
    private VBox wednesdayColumn;
    @FXML
    private Label wednesdayWeekDayLabel;
    @FXML
    private VBox thursdayColumn;
    @FXML
    private Label thursdayWeekDayLabel;
    @FXML
    private VBox fridayColumn;
    @FXML
    private Label fridayWeekDayLabel;
    @FXML
    private VBox saturdayColumn;
    @FXML
    private Label saturdayWeekDayLabel;

    @FXML
    private Button nextWeekButton;

    @FXML
    private Button previousWeekButton;

    protected int displayedWeekNumber;

    private LocalDate firstDayOfDisplayedWeek;
    private LocalDate lastDayOfDisplayedWeek;

//    protected
    private List<Label> weekdayLabels;

    private Map<Integer, VBox> dayToGrid = new HashMap<>();


    public WeeklyCalendarController (){
        super();
        this.firstDayOfDisplayedWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.lastDayOfDisplayedWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        this.initDisplayedWeekNumber();
//        System.out.println(DayOfWeek.SUNDAY.getValue());

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
        this.initWeekdayArray();
        this.initWeekYearLabel();
        this.setWeekdayLabels();
        this.displayAppointments();

    }

    private void displayAppointments() {
        ArrayList<Appointment> list = Appointment.getAllByWeek(this.firstDayOfDisplayedWeek, this.lastDayOfDisplayedWeek);
//        List<Appointment> appointments = Appointment.getAllByYearMonth(this.firstDayOfDisplayedMonth);
        for (Appointment ap :
                list) {
            int dayOfMonth = ap.getStart().getDayOfMonth();
            VBox target = this.dayToGrid.get(dayOfMonth);
            this.insertAppointmentBlob(target, ap);
        }
//        System.out.println(list);
//        int iDay = this.firstDayOfDisplayedWeek.getDayOfMonth();
//        //is a lambda going to work here? It should but I think we're approching it wrong
//        //foreach dayToGrid
//        //iterate over list and check to see if they belong in the day
//        for (int i = 0; i < 7; i++) {
//            VBox box = dayToGrid.get(i);
//            int aptDay = Integer.valueOf(box.getChildren().);
//
//            BiPredicate<Integer, Integer> belongs = (iDay,
//
//        }
//        list.stream().forEach( x -> {
//            int dayOfMonth = x.getStart().getDayOfMonth();
//            VBox target = dayToGrid.get(dayOfMonth);
//            System.out.println(target);
//            this.insertAppointmentBlob(target, x);
//        }); // get the day of the appt from start the put in the appropriate column
        //get
    }

    private void initWeekdayArray() {
        this.weekdayLabels = new ArrayList<Label>(Arrays.asList(sundayWeekDayLabel,mondayWeekDayLabel,tuesdayWeekDayLabel,wednesdayWeekDayLabel,
                thursdayWeekDayLabel,fridayWeekDayLabel,saturdayWeekDayLabel));
    }

    private void initWeekYearLabel() {
        initWeekYearLabel(this.firstDayOfDisplayedWeek, this.lastDayOfDisplayedWeek);
    }

    private void initWeekYearLabel(LocalDate firstDay, LocalDate lastDay) {
        String isSameYear = "";
        if (firstDay.getYear() != lastDay.getYear()) {
            isSameYear = "/" + firstDay.getYear();
        }
        String display = firstDay.getMonthValue() + "/" + firstDay.getDayOfMonth()+ isSameYear + " - " + lastDay.getMonthValue() +
                "/" + lastDay.getMonthValue() + "/" + lastDay.getYear();
        this.weekYearLabel.setText(display);

    }

    public void setWeekdayLabels(){
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String[] dayNames = symbols.getShortWeekdays();

        for (int i = 0; i < 7; i++) {
            LocalDate date = firstDayOfDisplayedWeek.plusDays(i);
            String display = dayNames[i+1] + " " + date.getMonthValue() + "/" + date.getDayOfMonth() ;
            Label label = weekdayLabels.get(i);
            label.setText(display);
            dayToGrid.put(date.getDayOfMonth(), (VBox) label.getParent());
        }
//        System.out.println("dayToGrid: "+dayToGrid);
    }


    public void showPreviousWeek(ActionEvent actionEvent) {
//        System.out.println("Display the previous week");
        LocalDate firstOfPreviousWeek = this.firstDayOfDisplayedWeek.minusWeeks(1);
        LocalDate lastOfPreviousWeek = this.lastDayOfDisplayedWeek.minusWeeks(1);
        this.initWeekYearLabel(firstOfPreviousWeek, lastOfPreviousWeek);
        this.firstDayOfDisplayedWeek = firstOfPreviousWeek;
        this.lastDayOfDisplayedWeek = lastOfPreviousWeek;
        this.setWeekdayLabels();

    }

    public void showNextWeek(ActionEvent actionEvent) {
//        System.out.println("display the next week");
        LocalDate firstOfNextWeek = this.firstDayOfDisplayedWeek.plusWeeks(1);
        LocalDate lastOfNextWeek = this.lastDayOfDisplayedWeek.plusWeeks(1);
        this.initWeekYearLabel(firstOfNextWeek, lastOfNextWeek);
        this.firstDayOfDisplayedWeek = firstOfNextWeek;
        this.lastDayOfDisplayedWeek = lastOfNextWeek;
        this.setWeekdayLabels();
    }

    private void initDisplayedWeekNumber() {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        this.displayedWeekNumber = currentDate.get(weekOfYear);
    }




    public void setDisplayedWeekNumber(int displayedWeekNumber) {
        this.displayedWeekNumber = displayedWeekNumber;
    }
}
