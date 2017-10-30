package calendar.models;

import java.time.Instant;
import java.time.ZonedDateTime;

abstract public class ModelBuilder {

    protected String createdBy;

    protected ZonedDateTime createDate;

    protected Instant lastUpdate;

    protected String lastUpdateBy;

    protected String tableName;



    public ModelBuilder setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ModelBuilder setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public ModelBuilder setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public ModelBuilder setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
        return this;
    }

    public ModelBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    protected ModelBuilder checkAndSetCreate(){
        System.out.println(createDate + createdBy + lastUpdate +lastUpdateBy);
        if (createDate == null) {
            this.createDate = ZonedDateTime.now();
        }
        if (createdBy == null) {
            //todo make this access a static variable with the username in it
            this.createdBy = "Wade";
        }
        return this;
    }

    protected ModelBuilder checkAndSetUpdate(){
//        System.out.println(createDate + createdBy + lastUpdate +lastUpdateBy);
        this.lastUpdate = Instant.now();
        //todo make this access a static variable with the username in it
        this.lastUpdateBy = "Wade";
        return this;
    }

}
