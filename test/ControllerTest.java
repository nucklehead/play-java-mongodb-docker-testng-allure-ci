import de.flapdoodle.embed.mongo.MongoImportStarter;
import de.flapdoodle.embed.mongo.config.IMongoImportConfig;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.mongo.config.Net;
import org.testng.annotations.*;
import play.test.*;
import play.Application;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import models.*;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.ws.*;
import play.libs.Json;

import com.mongodb.Mongo;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.config.MongoImportConfigBuilder;

public class ControllerTest {
    int timeout = 4000;
    Application app;
    ObjectNode dataOk;
    ObjectNode dataError1;
    ObjectNode dataError2;
    MongodExecutable mongoExecutable;
    MongodProcess mongoProcess;
    Mongo mongo;
    final int MONGO_TEST_PORT = 27028;
    final String DB_NAME = "play-java-test";
    final boolean upsert = true;
    final boolean drop = true;
    final boolean jsonArray = true;
    final String jsonFile = "/home/jpierre/nuckle-seeds/play-java-mongodb-docker-testng-allure-ci/test/resources/task_dataset_1.json";
    private final String collection = "features";

    public ControllerTest() {
        dataOk = Json.newObject();
        dataOk.put("name", "New task");
        dataOk.put("description", "Just adding.");

        dataError1 = Json.newObject();
        dataError1.put("name", "");

        dataError2 = Json.newObject();
    }

    // Data needed for create the fake
    private HashMap<String, String> settings() {
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("mongodb.uri", "mongodb://localhost:"+ MONGO_TEST_PORT +"/" + DB_NAME);
        settings.put("application.global", "Global.Global");
        return(settings);
    }

    @BeforeClass
    public void createTables() throws IOException {
        IMongodConfig mongoConfigConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(MONGO_TEST_PORT, Network.localhostIsIPv6()))
                .configServer(false)
                .build();

        mongoExecutable = MongodStarter.getDefaultInstance().prepare(mongoConfigConfig);
        mongoProcess = mongoExecutable.start();
        mongo = new Mongo("localhost", MONGO_TEST_PORT);
        mongo.getDB(DB_NAME);

        Application fakeApp = Helpers.fakeApplication(settings());
        running (fakeApp, new Runnable() {
            public void run() {
            }
        });
    }

    @BeforeMethod
    public void initializeData() throws Exception {

        IMongoImportConfig mongoImportConfig = new MongoImportConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(MONGO_TEST_PORT, Network.localhostIsIPv6()))
                .db(DB_NAME)
                .collection(collection)
                .upsert(upsert)
                .dropCollection(drop)
                .jsonArray(jsonArray)
                .importFile(jsonFile)
                .build();

        MongoImportStarter.getDefaultInstance().prepare(mongoImportConfig).start();
    }

    @AfterMethod
    public void closeDB() throws Exception {
        if (mongoExecutable != null) {
            mongoExecutable.stop();
        }
        mongo.close();
        mongoProcess.stop();
    }

    @AfterClass
    public void closeDBClient() throws Exception {
        mongo.close();
        mongoProcess.stop();
    }

    @Test
    public void testFindTask() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks/1")
                    .get()
                    .get(timeout);

            assertEquals(OK, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("id"), 1);
            assertEquals(responseJson.get("name"), "Wash car");
        });
    }

    @Test
    public void testFindTaskNotFound() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks/5")
                    .get()
                    .get(timeout);

            assertEquals(NOT_FOUND, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("error"), "Not found 5");
        });
    }

    @Test
    public void testPageTasks() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .get()
                    .get(timeout);

            assertEquals(OK, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertTrue(responseJson.get("data").isArray());
            assertEquals(responseJson.get("data").size(), 3);
            assertEquals(responseJson.get("total"), 4);
            assertNotNull(responseJson.get("link-self"));
            assertNotNull(responseJson.get("link-next"));
            assertNull(responseJson.get("link-prev"));
        });
    }

    @Test
    public void testCreateTask() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .post(dataOk)
                    .get(timeout);

            assertEquals(CREATED, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("id"), 5);
            assertEquals(responseJson.get("name"), "New task");
        });
    }

    @Test
    public void testCreateTaskBadRequest1() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .post(dataError1)
                    .get(timeout);

            assertEquals(BAD_REQUEST, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("name"), "This field is required");
        });
    }

    @Test
    public void testCreateTaskBadRequest2() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .post(dataError2)
                    .get(timeout);

            assertEquals(BAD_REQUEST, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("name"), "This field is required");
        });
    }

    @Test
    public void testUpdateTask() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .put(dataOk.put("id", 1))
                    .get(timeout);

            assertEquals(OK, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("id"), 1);
            assertEquals(responseJson.get("name"), "New task");
        });
    }

    @Test
    public void testUpdateTaskBadRequest1() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .put(dataError1.put("id", 1))
                    .get(timeout);

            assertEquals(BAD_REQUEST, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("name"), "This field is required");
        });
    }

    @Test
    public void testUpdateTaskBadRequest2() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks")
                    .put(dataError2.put("id", 2))
                    .get(timeout);

            assertEquals(BAD_REQUEST, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("name"), "This field is required");
        });
    }

    @Test
    public void testDeleteTask() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks/1")
                    .delete()
                    .get(timeout);

            assertEquals(OK, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("msg"), "Deleted 1");
        });
    }

    @Test
    public void testDeleteTaskNotFound() {
        running(testServer(3333, app), () -> {
            WSResponse response = WS
                    .url("http://localhost:3333/tasks/5")
                    .delete()
                    .get(timeout);

            assertEquals(NOT_FOUND, response.getStatus());
            assertEquals("application/json; charset=utf-8", response.getHeader("Content-Type"));

            JsonNode responseJson = response.asJson();
            assertTrue(responseJson.isObject());
            assertEquals(responseJson.get("error"), "Not found 5");
        });
    }
}
