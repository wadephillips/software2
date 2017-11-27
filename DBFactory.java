package calendar;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * A class to provide a connection to the database.  This class pulls all of the database access credentials from the
 * database.properties file, which is located at the root of the project.
 */
public class DBFactory {

    /**
     * Get a DataSource based on the properties contained in the database.properties file
     * @return DataSource
     */
    public static DataSource get() {
        Properties properties = null;
        FileInputStream fileInput = null;
        MysqlDataSource dataSource = null;
        try{
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File file = new File(rootPath + "database.properties");
            fileInput = new FileInputStream(file);

            properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            dataSource = new MysqlDataSource();

            dataSource.setURL(properties.getProperty("DB_URL"));
            dataSource.setUser(properties.getProperty("DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("DB_PASSWORD"));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
