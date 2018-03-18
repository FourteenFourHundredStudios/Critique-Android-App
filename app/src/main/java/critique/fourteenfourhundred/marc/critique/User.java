package critique.fourteenfourhundred.marc.critique;

/**
 * Created by Marc on 17/3/18.
 */

public class User {

    String username="";
    boolean selected = false;


    public User(String username){
        this.username=username;
    }

    public boolean getSelected(){
        return selected;
    }

    void toggleSelected(){
        selected = !selected;
    }

    public String getUsername(){
        return username;
    }

}
