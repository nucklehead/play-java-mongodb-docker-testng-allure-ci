package datasources;

/**
 * Created by ntenisOT on 16/10/14.
 */

import com.mongodb.MongoClient;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;

public class MorphiaObject {

    static public MongoClient mongo;

    static public Morphia morphia;

    static public Datastore datastore;
}
