package models;

import datasources.MorphiaObject;
import com.google.code.morphia.query.Query;
import java.util.List;


public class TaskDAO {

    /**
     * Create an Task
     *
     * @param Task model
     *
     * @return Task
     */
    public static Task create (Task model) {
        MorphiaObject.datastore.save(model);
        return model;
    }

    /**
     * Find an Task by id
     *
     * @param Integer id
     *
     * @return Task
     */
    public static Task find(Integer id) {
        return MorphiaObject.datastore.find(Task.class, "Id", id).get();
    }

    /**
     * Update an Task
     *
     * @param Task model
     *
     * @return Task
     */
    public static Task update(Task model) {
        return find(new Integer(MorphiaObject.datastore.merge(model).getId().toString()));

    }

    /**
     * Delete an Task by id
     *
     * @param Integer id
     */
    public static void delete(Integer id) {
        MorphiaObject.datastore.delete(Task.class, id);
    }

    /**
     * Get all Tasks
     *
     * @return List<Task>
     */
    public static List<Task> all() {
        Query<Task> result = MorphiaObject.datastore.createQuery(Task.class);
        return result.asList();
    }

    /**
     * Get the page of Tasks
     *
     * @param Integer page
     * @param Integer size
     *
     * @return List<Task>
     */
    public static List<Task> paginate(Integer page, Integer size) {
        Query<Task> result = MorphiaObject.datastore.createQuery(Task.class).offset(page * size).limit(size);
        return result.asList();
    }

    /**
     * Get the number of total row
     *
     * @return Long
     */
    public static Long count() {
        return  MorphiaObject.datastore.createQuery(Task.class).countAll();
    }
}