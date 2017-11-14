package calendar.models;

import calendar.ModelDAO;

import javax.jws.soap.SOAPBinding;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by wadelp on 10/17/17.
 */
public class User extends Model {

    /**
     * The user's id
     */
    private Long userId;

    /**
     * The username sign in credential
     */
    private String userName;

    /**
     * The users password
     */
    private String password;

    /**
     * Is the user active
     */
    private int active;


    public User(){
        super();

    }

    public User(long userId,
                String userName,
                String password,
                int active,
                String createdBy,
                ZonedDateTime createDate,
                Instant lastUpdate,
                String lastUpdateby) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.lastUpdateby = lastUpdateby;
    }

    /**
     * Saves a new User record to the database
     *
     * @return
     */
    public User save() {
        if (userId > 0) {
            //todo throw an exception
        }
        String nameCheckSql = "SELECT COUNT(userId) as count FROM user WHERE userName = ?;";

        String sql = "INSERT INTO user (userId, userName, password, active, createBy, createDate, lastUpdate, lastUpdatedBy) " +
                "VALUES (?,?,?,?,?,?,?,?);";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement ncStmt = conn.prepareStatement(nameCheckSql);
        ) {
            ncStmt.setString(1, this.userName);
            ResultSet nameCheckResult = ncStmt.executeQuery();
            nameCheckResult.first();
            int userExists = nameCheckResult.getInt("count");

            if (userExists >= 1) {
                //todo thow an exception or alert
                System.out.println(" that username already exists!");
            } else {

                conn.setAutoCommit(false);
                Savepoint savepoint1 = conn.setSavepoint();
                try {
                    this.userId = this.getNextId();

                    stmt.setLong(1, this.userId);
                    stmt.setString(2, this.userName);
                    stmt.setString(3, this.password);
                    stmt.setInt(4, this.active);
                    stmt.setString(5, super.getCreatedBy());
                    stmt.setTimestamp(6, Timestamp.from(this.getCreateDate().toInstant()));//datetime
                    stmt.setTimestamp(7, Timestamp.from(this.getLastUpdate())); //timestamp
                    stmt.setString(8, this.getLastUpdateby());

                    stmt.executeUpdate();
                    conn.commit();

                } catch (SQLException e) {
                    e.printStackTrace();
                    conn.rollback(savepoint1);
                }

            }
        } catch(SQLException e){
            //            System.out.println(sql);
            e.printStackTrace();

        }

        return this;
    }


    /**
     * method to retrieve all instances of the entity from the database
     */
    public static ArrayList<User> findAll() {
        String sql = "SELECT * FROM user;";
        ArrayList<User> list = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);    ){

            while (rs.next()){
                list.add(buildUserFromDB(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * method to persist changes on the entity to the database.
     *

     * @return
     */
    public boolean update() {
        if (userId == null) {
            //todo throw an exception
        }
        boolean updated = false;
        String sql = "UPDATE user SET userName=?, password=?, active=?, lastUpdate=?, lastUpdatedBy=? " +
                "WHERE userId = ?;";
//        System.out.println(sql);
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            conn.setAutoCommit(false);
            Savepoint savepoint1 = conn.setSavepoint();
            try {
               this.checkAndSetUpdate();


                stmt.setString(1, this.userName);
                stmt.setString(2, this.password);
                stmt.setInt(3, this.active);
                stmt.setTimestamp(4, Timestamp.from(this.getLastUpdate())); //timestamp
                //todo need to change this so that it gets the updating users name
                stmt.setString(5, this.getLastUpdateby());
                stmt.setLong(6, this.userId);
//                System.out.println(stmt);
                int rs = stmt.executeUpdate();
//                System.out.println(rs);
                conn.commit();
                if (rs == 1) {
                    updated = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback(savepoint1);
            }


        } catch (SQLException e) {
//            System.out.println(sql);
            e.printStackTrace();

        }
        return updated;
    }

    /**
     * method to retrieve an instance of the entity from the database.
     *
     * @return
     */
    public static User findById(long id) {
        String sql = "SELECT * FROM user WHERE userid = " + id + ";";
        User user = null;
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery(sql)){
//            System.out.println();
            if(resultSet.first()){
                user = buildUserFromDB(resultSet);
            }

        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }

        return user;
    }

    public static User buildUserFromDB(ResultSet resultSet) throws SQLException {
        ZoneId zone = ZoneId.systemDefault();
        UserBuilder userBuidler = new UserBuilder();
        userBuidler.setUserId(resultSet.getInt("userid"))
                .setUserName(resultSet.getString("userName"))
                .setPassword(resultSet.getString("password"))
                .setActive(resultSet.getInt("active"))
                .setCreatedBy(resultSet.getString("createBy"))
                .setLastUpdateBy(resultSet.getString("lastUpdatedBy"))
                .setLastUpdate(resultSet.getTimestamp("lastUpdate").toInstant())
                .setCreateDate(ZonedDateTime.ofInstant(resultSet.getTimestamp("createDate").toInstant(), zone));
        return userBuidler.build();
    }


    /**
     * method to delete a User objects record from the DB record from the database.
     *
     * @return
     */
    public boolean delete() {
        return deleteById(this.userId);
    }

    /**
     * Static method to delete a User record from the database.
     *
     * @return
     */
    public static Boolean deleteById(long id) {
        Boolean result = false;
        String sql = "DELETE FROM user WHERE userId = ?;";
        try (Connection conn = DATASOURCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setLong(1, id);
            int rs = stmt.executeUpdate();

            if (rs == 1) {
                result = true;
            }


        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }
        return result;
    }


    public String getUserName() {
        return this.userName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.userName + " is active: " + isActive();
    }


    //##################### GETTERS and SETTERS ###################

    public boolean isActive() {
        boolean isActive = false;
        if (this.active != 0) isActive = true;
        return isActive;
    }

    public void setActive(boolean active) {
        if (active) {
            this.active = 1;
        } else {
            this.active = 0;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
