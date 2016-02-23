package Global; /**
 * Created by ntenisOT on 16/10/14.
 */

import datasources.MongoDB;
import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application started!");

        MongoDB.connect();

        Logger.info("Connected to Database!");

        initialData();
    }

    public void onStop(Application app) {
        Logger.info("Appplication stopped!");
        MongoDB.disconnect();
    }

    // Update to add a role

    private void initialData() {

//        if (MorphiaObject.datastore.createQuery(SecurityRole.class).countAll() == 0) {
//            for (final String roleName : Arrays
//                    .asList(controllers.Application.USER_ROLE)) {
//                final SecurityRole role = new SecurityRole();
//                role.roleName = roleName;
//                MorphiaObject.datastore.save(role);
//            }
//        }
    }
}
