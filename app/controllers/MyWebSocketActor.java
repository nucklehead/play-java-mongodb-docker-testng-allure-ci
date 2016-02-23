package controllers;

import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import models.TaskService;
import play.libs.Json;

/**
 * Created by jpierre on 2/20/16.
 */
public class MyWebSocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object task) throws Exception {
        if (task instanceof JsonNode) {
            JsonNode jsonNode = (JsonNode)task;
            Task newTask = Json.fromJson(jsonNode, Task.class);
            newTask.status = "pending";
            TaskService.create(newTask);
            out.tell(Json.toJson(newTask), self());
        }
    }

}

