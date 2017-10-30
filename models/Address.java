package calendar.models;

import calendar.ModelDAO;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by wadelp on 10/17/17.
 */
public class Address extends Model {

    public Address(){
        super();

    }

    /**
     * method to return an empty version of the entity.
     *
     * @return
     */

    public Model create() {
        return null;
    }

    /**
     * method to retrieve an instance of the entity from the database.
     *
     * @param id
     * @return
     */

    public Model find(int id) {
        String sql = "SELECT * FROM address WHERE addressId = " + id + ";";
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery(sql)){
            System.out.println();
            for (Field field : this.getFields()){
                System.out.println(field.getName());
            }
            while (resultSet.next()){
                System.out.println(resultSet.getString(2));
            }

        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * method to retrieve all instances of the entity from the database
     */

    public ArrayList<ModelDAO> findAll() {
        return null;
    }

    /**
     * method to persist changes on the entity to the database.
     *
     * @param id
     * @return
     */

    public Model update(int id) {
        return null;
    }

    /**
     * method to help save changes the entity to the database.
     *
     * @return
     */

    public Model save() {
        return null;
    }

    /**
     * method to delete the entitie's record from the database.
     *
     * @param id
     * @return
     */

    public Model delete(int id) {
        return null;
    }
}
