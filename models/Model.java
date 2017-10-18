package calendar.models;

import calendar.ModelDAO;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by wadelp on 10/17/17.
 */
abstract public class Model implements ModelDAO{

    private String createdBy;

    private ZonedDateTime CreateDate;

    private Instant lastUpdate;

    private String lastUpdateby;

    public Model(){

    }

//   todo create methods to connect to db and for CRUD operations

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        CreateDate = createDate;
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


//

}
