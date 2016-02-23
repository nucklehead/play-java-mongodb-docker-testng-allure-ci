package models;

import java.util.List;

public class TaskService {
    /**
     * Create an Task
     *
     * @param Task data
     *
     * @return Task
     */
    public static Task create(Task data) {
        return TaskDAO.create(data);
    }

    /**
     * Update an Task
     *
     * @param Task data
     *
     * @return Task
     */
    public static Task update(Task data) {
        return TaskDAO.update(data);
    }

    /**
     * Find an Task by id
     *
     * @param Integer id
     *
     * @return Task
     */
    public static Task find(Integer id) {
        return TaskDAO.find(id);
    }

    /**
     * Delete an Task by id
     *
     * @param Integer id
     */
    public static Boolean delete(Integer id) {
        Task task = TaskDAO.find(id);
        if (task != null) {
            TaskDAO.delete(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get all Tasks
     *
     * @return List<Task>
     */
    public static List<Task> all() {
        return TaskDAO.all();
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
        return TaskDAO.paginate(page, size);
    }

    /**
     * Get the number of total of Tasks
     *
     * @return Long
     */
    public static Long count() {
        return TaskDAO.count();
    }
}