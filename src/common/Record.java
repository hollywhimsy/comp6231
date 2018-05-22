package common;



/**
 * Represents a record
 * This is an abstract class as it is inherited by TeacherRecord and StudentRecord classes 
 */
public interface Record {
    String getId();
    void setId(String Id);

}
