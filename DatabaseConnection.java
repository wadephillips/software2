package calendar;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private String username;

    private String password;

    private String databaseName;

    private String location;

    private String port;

    private Connection connection;

    private static final DatabaseConnection instance = new DatabaseConnection();

    private DatabaseConnection(){
        try {
            File file = new File("database.properties");
            FileInputStream fileInput = new FileInputStream(file);

            Properties properties = new Properties();
            properties.load(fileInput);

            this.username = properties.getProperty("username");

            this.password = properties.getProperty("password");

            this.databaseName = properties.getProperty("databaseName");

            this.location = properties.getProperty("location");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        this.setConnection();

    }

    private void setConnection() {
        try {
            String url = "jdbc:mysql:" + this.location + "/" + this.databaseName;
            this.connection = DriverManager.getConnection(url, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Failed to create connection");
        }
    }

    public static DatabaseConnection getInstance() {
        return instance;
    }

    public static Connection getConnection() { return instance.getConnection(); }
}
