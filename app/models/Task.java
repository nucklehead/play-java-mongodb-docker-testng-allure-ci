package models;

import org.bson.types.ObjectId;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import play.data.validation.Constraints;


@Entity
public class Task {
    public static String TABLE = Task.class.getSimpleName();

    @Id
    public ObjectId id;

    @Constraints.Required
    public String name;

    public String description;

    public String status;



    public Task() {}
    public Task(String name) { this.name = name; }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "pending";
    }

    /**
     * Set all empty values to null
     */
    public void emptyToNull() {
        if (name != null && name.isEmpty()) name = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Task aux = (Task) obj;

        if (id != null && aux.id != null)
            return (id == aux.id);
        else
            return (name.equals(aux.name));
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}