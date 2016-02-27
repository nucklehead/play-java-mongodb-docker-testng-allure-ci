package controllers;

import java.util.List;

import play.mvc.*;

import play.libs.Json;
import play.data.Form;
import views.html.*;


import models.*;

import akka.actor.*;
import play.libs.F.*;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

public class TaskController extends Controller {
    static Form<Task> taskForm = Form.form(Task.class);

    /**
     * Add the content-type json to response
     *
     * @param Result httpResponse
     *
     * @return Result
     */
    public static Result jsonResult(Result httpResponse) {
        response().setContentType("application/json; charset=utf-8");
        return httpResponse;
    }

    /**
     * Get the index page
     *
     * @return Result
     */
    public Result index() {
        return ok(index.render("API REST for JAVA Play Framework"));
    }

    /**
     * Get the tasks with pagination
     *
     * @param Integer page
     * @param Integer size
     *
     * @return Result
     */
    public Result list(Integer page, Integer size) {
        List models = TaskService.paginate(page-1, size);
        Long count = TaskService.count();

        ObjectNode result = Json.newObject();
        result.put("data", Json.toJson(models));
        result.put("total", count);
        if (page > 1) result.put("link-prev", routes.TaskController.list(page-1, size).toString());
        if (page*size < count) result.put("link-next", routes.TaskController.list(page+1, size).toString());
        result.put("link-self", routes.TaskController.list(page, size).toString());

        return jsonResult(ok(result));
    }

    /**
     * Get the tasks with pagination
     *
     * @param Integer page
     * @param Integer size
     *
     * @return List
     */
    public static List<Task> listTasks() {
        return TaskService.all();
    }

    /**
     * Get one task by id
     *
     * @param Integer id
     *
     * @return Result
     */
    public Result get(Integer id) {
        Task task = TaskService.find(id);
        if (task == null ) {
            ObjectNode result = Json.newObject();
            result.put("error", "Not found " + id);
            return jsonResult(notFound(result));
        }
        return jsonResult(ok(Json.toJson(task)));
    }

    /**
     * Create an task with the data of request
     *
     * @return Result
     */
    public Result create() {
        Form<Task> task = taskForm.bindFromRequest();
        if (task.hasErrors()) {
            return jsonResult(badRequest(task.errorsAsJson()));
        }
        Task newTask = TaskService.create(task.get());
        return jsonResult(created(Json.toJson(newTask)));
    }

    /**
     * Update an task with the data of request
     *
     * @return Result
     */
    public Result update() {
        Form<Task> task = taskForm.bindFromRequest();
        if (task.hasErrors()) {
            return jsonResult(badRequest(task.errorsAsJson()));
        }
        Task updatedTask = TaskService.update(task.get());
        return jsonResult(ok(Json.toJson(updatedTask)));
    }

    /**
     * Delete an task by id
     *
     * @param Integer id
     *
     * @return Result
     */
    public Result delete(Integer id) {
        if (TaskService.delete(id)) {
            ObjectNode result = Json.newObject();
            result.put("msg", "Deleted " + id);
            return jsonResult(ok(result));
        }
        ObjectNode result = Json.newObject();
        result.put("error", "Not found " + id);
        return jsonResult(notFound(result));
    }

    public WebSocket<JsonNode> socket() {
        return WebSocket.withActor(MyWebSocketActor::props);
    }
}
