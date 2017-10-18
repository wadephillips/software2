package calendar;

import calendar.models.Model;

import java.util.ArrayList;

public interface ModelDAO {

    /**
     * method to return an empty version of the entity.
     * @return
     */
    public Model create();

    /**
     * method to retrieve an instance of the entity from the database.
     * @return
     */
    public Model find(int id);

    /**
     * method to retrieve all instances of the entity from the database
     */
    public ArrayList<Model> findAll();

    /**
     *  method to persist changes on the entity to the database.
     * @return
     */
    public Model update(int id);

    /**
     *  method to help save changes the entity to the database.
     * @return
     */
    public Model save();

    /**
     *  method to delete the entitie's record from the database.
     * @return
     */
    public Model delete(int id);
}
