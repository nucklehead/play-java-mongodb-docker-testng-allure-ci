import org.aspectj.lang.annotation.Before;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import play.test.*;
import play.Application;

import static play.test.Helpers.*;
import static org.junit.Assert.*;
import java.util.List;
import models.*;
//import org.dbunit.*;
//import org.dbunit.dataset.*;
//import org.dbunit.dataset.xml.*;
import java.util.HashMap;
import java.io.FileInputStream;

import play.libs.ws.*;

public class ModelTest {
//    JndiDatabaseTester databaseTester;
    Application app;

    // Data needed for create the fake
    private static HashMap<String, String> settings() {
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("db.default.url", "jdbc:mysql://api.template-java.com:3306/play_test");
        settings.put("db.default.username", "root");
        settings.put("db.default.password", "");
        settings.put("db.default.jndiName", "DefaultDS");
        settings.put("jpa.default", "mySqlPersistenceUnit");
        return(settings);
    }

    @BeforeClass
    public static void createTables() {
        Application fakeApp = Helpers.fakeApplication(settings());
        running (fakeApp, () -> {
//            JPA.withTransaction(() -> {});
        });
    }

    @BeforeMethod
    public void initializeData() throws Exception {
//        app = Helpers.fakeApplication(settings());
//        databaseTester = new JndiDatabaseTester("DefaultDS");
//        IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/employee_dataset_1.xml"));
//        databaseTester.setDataSet(initialDataSet);
//        databaseTester.onSetup();
    }

    @AfterMethod
    public void closeDB() throws Exception {
//        databaseTester.onTearDown();
    }

    @Test
    public void testFindTask() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                Task e = TaskService.find(1);
//                assertEquals(e.name, "Josrom");
//            });
        });
    }

    @Test
    public void testFindTaskNotFound() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                Task e = TaskService.find(5);
//                assertNull(e);
//            });
        });
    }

    @Test
    public void testFindAllTasks() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                List<Task> e = TaskService.all();
//                long count = TaskService.count();
//                assertEquals(count, 4);
//
//                assertTrue(e.contains(new Task("Josrom")));
//                assertTrue(e.contains(new Task("Dantar")));
//                assertTrue(e.contains(new Task("Ericmaster")));
//                assertTrue(e.contains(new Task("xChaco")));
//            });
        });
    }

    @Test
    public void testPageTasks() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                List<Task> e = TaskService.paginate(0, 3);
//
//                assertTrue(e.contains(new Task("Josrom")));
//                assertTrue(e.contains(new Task("Dantar")));
//                assertTrue(e.contains(new Task("Ericmaster")));
//                assertFalse(e.contains(new Task("xChaco")));
//
//                e = TaskService.paginate(1, 3);
//                assertEquals(e.size(), 1);
//            });
        });
    }

    @Test
    public void testCreateTask() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                Task create = new Task("New test");
//                Task e = TaskService.create(create);
//                assertEquals(e, create);
//            });
        });
    }

    @Test
    public void testUpdateTask() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                Task create = new Task("New test");
//                Task e = TaskService.create(create);
//                e.name = "Update test";
//                Task update = TaskService.update(e);
//                assertEquals(update.name, "Update test");
//            });
        });
    }

    @Test
    public void testDeleteTask() {
        running (app, () -> {
//            JPA.withTransaction(() -> {
//                Task create = new Task("New test");
//                Task e = TaskService.create(create);
//
//                assertTrue(TaskService.delete(e.id));
//                assertFalse(TaskService.delete(e.id));
//            });
        });
    }
}
