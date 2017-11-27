package calendar.models;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Creates an object to represent users in the application.  This model also handles interactions with the user database table
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

    /**
     * The constructor
     */
    public User(){
        super();
    }

    /**
     * The constructor for a user that has already been saved to the database.
     * @param userId
     * @param userName
     * @param password
     * @param active
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     */
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
     * @return this
     */
    public User save() {
        if (userId > 0) {
            this.update();
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
            e.printStackTrace();

        }

        return this;
    }


    /**
     * Lookup and return a list of all Users with records in the database
     *
     * @return  A list of customers
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
     * Updates an User record that already exists in the database;
     *
     * @return true if the update was successful, false if not
     */
    public boolean update() {
        if (userId == null) {
            this.save();
        }
        boolean updated = false;
        String sql = "UPDATE user SET userName=?, password=?, active=?, lastUpdate=?, lastUpdatedBy=? " +
                "WHERE userId = ?;";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            conn.setAutoCommit(false);
            Savepoint savepoint1 = conn.setSavepoint();
            try {
               this.setUpdate();


                stmt.setString(1, this.userName);
                stmt.setString(2, this.password);
                stmt.setInt(3, this.active);
                stmt.setTimestamp(4, Timestamp.from(this.getLastUpdate())); //timestamp
                //todo check to make sure that this is getting the correct username
                stmt.setString(5, this.getLastUpdateby());
                stmt.setLong(6, this.userId);
                int rs = stmt.executeUpdate();
                conn.commit();
                if (rs == 1) {
                    updated = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback(savepoint1);
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }
        return updated;
    }

    /**
     * Look up a specific user's record by id
     *
     * @return a User object
     */
    public static User findById(long id) {
        String sql = "SELECT * FROM user WHERE userid = " + id + ";";
        User user = null;
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery(sql)){
            if(resultSet.first()){
                user = buildUserFromDB(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Helper method to build up a User object after a database query
     * @param resultSet a record from the user table in the database
     * @return a User object
     * @throws SQLException
     */
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
     * Delete a User record with a known id from the database.
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
            e.printStackTrace();
        }
        return result;
    }


    /**
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.userName + " is active: " + isActive();
    }


    /**
     * Getters and Setters
     */

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

    public String getUserName() {
        return this.userName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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
