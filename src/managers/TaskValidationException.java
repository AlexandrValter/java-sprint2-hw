package managers;

public class TaskValidationException extends RuntimeException{

    public TaskValidationException(String s) {
        System.out.println(s);
    }
}