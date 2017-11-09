package calendar;

import calendar.controllers.LoginController;
import calendar.controllers.MainController;
import calendar.models.User;
import calendar.models.UserBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Locale;

public class Main extends Application {

    static DataSource DATASOURCE = DBFactory.get();

    private static Locale locale;

    private static User loggedInUser;

    /**
     * Holds the primary stage
     */
    private static Stage mainStage;




    @Override
    public void start(Stage primaryStage) throws Exception{
//        this.locale = Locale.getDefault();
//        Locale locale = new Locale("es", "ES");
//        Locale.setDefault(locale);
//        UserBuilder userBuilder = new UserBuilder();
//        userBuilder.setActive(1).setUserName("Xander").setPassword("pepelepew");
//                .setCreatedBy("wade").setCreateDate(ZonedDateTime.now())
//                .setLastUpdate(Instant.now()).setLastUpdateBy("wade");

//        User user = User.findById(7);
//        System.out.println(user.toString());
//        ArrayList<User> list = User.findAll();
//        for (User user :
//                list) {
//            System.out.println(user);
//        }
//        System.out.println(user.delete());
//        User user = userBuilder.build();
//        user.save();
//        System.out.println(user.getUserId());
//        user.setUserName("Candra");
//        System.out.println(user.getUserName());
//        user.update();
//        boolean success = User.deleteById(7);
//        System.out.println(success);
//        System.out.println(user.getNextId());
//        Address address = new Address();
//        address.find(1);
//        while (resultSet.next()){
//            System.out.println(resultSet.getString(2));
//        }


//        ArrayList<ModelDAO> users = new User().findAll();
//        System.out.println(users);
//        for (ModelDAO user: users
//             ) {
//            User user2 = (User) user;
//            System.out.println(user2.getUserName());
//        }
//        DataSource dataSource = DBFactory.get();
//        try (Connection conn = DATASOURCE.getConnection();
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM user; ");){
//
//            while (rs.next()){
//                System.out.println(rs.getString(2
//                ));
//            }
//        }

//        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.mainStage = primaryStage;
        //todo change back to login.fxml
//        FXMLLoader root = new FXMLLoader(getClass().getResource("navigation.fxml"));
        FXMLLoader root = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root.load());

        //todo change back to Login Controller
        LoginController controller = root.<LoginController>getController();
//        MainController controller = root.getController();
        controller.localize();
//        controller.loadContent("monthCalendar.fxml");

        primaryStage.setTitle("ACME Calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void setLoggedInUser(User loggedInUser) {
        Main.loggedInUser = loggedInUser;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Locale getLocale() {
        return locale;
    }
}
