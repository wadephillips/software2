package calendar.models;

import calendar.Main;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * An abstract builder class for handling common methods and instance variables
 */
abstract public class ModelBuilder {

    /**
     * The user who created the Model
     */
    protected String createdBy;

    /**
     * The date and time when the Modle instance was created
     */
    protected ZonedDateTime createDate;

    /**
     * The time when this Model instance was last updated
     */
    protected Instant lastUpdate;

    /**
     * The user who last updated this Model instance
     */
    protected String lastUpdateBy;

    /**
     * The name of the database table that contains this Model instances record
     */
    protected String tableName;


    /**
     * Setters
     */

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

    /**
     * Check to see if the createDate and createdBy fields have been set and if not set them.
     * @return this
     */
    protected ModelBuilder checkAndSetCreate(){
        if (createDate == null) {
            this.createDate = ZonedDateTime.now();
        }
        if (createdBy == null) {
            this.createdBy = Main.getLoggedInUser().getUserName();
        }
        return this;
    }


}
