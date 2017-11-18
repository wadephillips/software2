package calendar;

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

    private String url;

    private Connection connection;

    private static final DatabaseConnection instance = new DatabaseConnection();

//    private static final Connection connection = instance.getConnection();

    private DatabaseConnection(){
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File file = new File(rootPath + "database.properties");
            FileInputStream fileInput = new FileInputStream(file);

            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            this.username = properties.getProperty("DB_USERNAME");

            this.password = properties.getProperty("DB_PASSWORD");

            this.databaseName = properties.getProperty("DB_NAME");

            this.location = properties.getProperty("DB_LOCATION");
            this.url = properties.getProperty("DB_URL");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        this.setConnection();

    }

    private void setConnection() {
        try {
//            String url = "jdbc:mysql://" + this.location + "/" + this.databaseName;
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        return instance;
    }

    public Connection getConnection() { return this.connection; }
}
