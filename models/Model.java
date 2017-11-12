package calendar.models;

import calendar.DBFactory;
import calendar.Main;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by wadelp on 10/17/17.
 */
abstract public class Model {

    protected String createdBy;

    protected ZonedDateTime createDate;

    protected Instant lastUpdate;

    protected String lastUpdateby;

    protected String tableName;

    protected Class currentClass;

    protected Field[] fields;

    static final DataSource DATASOURCE = DBFactory.get();

    public Model(){

        this.currentClass = this.getClass();
        this.tableName = this.currentClass.getSimpleName().toLowerCase();
        this.fields = this.currentClass.getDeclaredFields();

    }

    public Model(String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        this();
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.lastUpdateby = lastUpdateby;

    }

    //    abstract public Model find(int id);


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateby() {
        return lastUpdateby;
    }

    public void setLastUpdateby(String lastUpdateby) {
        this.lastUpdateby = lastUpdateby;
    }

    public String getTableName() {
        return tableName;
    }

    public Field[] getFields() {
        return fields;
    }

    public String getIdName() {
        return this.tableName + "Id";
    }

    protected long getNextId() {
        String sql = "SELECT MAX(`"+ this.getIdName() +"`) FROM " + this.tableName +";";
        long nextId = 0;
//        System.out.println(this.getIdName());
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);) {

            if(rs.first()){
                int lastId = rs.getInt(1);
                nextId = lastId + 1;
            } else {
                nextId = 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }
    protected Model checkAndSetCreate(){
//        System.out.println(createDate + createdBy + lastUpdate +lastUpdateby);
        if (createDate == null) {
            this.createDate = ZonedDateTime.now();
        }
        if (createdBy == null) {
            //todo make this access a static variable with the username in it
            this.createdBy = Main.getLoggedInUser().getUserName();
        }
//        System.out.println("helloz: " + createDate + createdBy + lastUpdate +lastUpdateby);
        return this;
    }


    protected Model checkAndSetUpdate(){
        this.lastUpdate = Instant.now();
        //todo make this access a static variable with the username in it
        this.lastUpdateby = Main.getLoggedInUser().getUserName();
        return this;
    }
}
