package com.fourteenfourhundred.critique.UI.Views;

public class Post {

    String username;
    String content;
    int votes;


    public Post(String username, String content, int votes){
        this.username=username;
        this.content=content;
        this.votes=votes;
    }

    public String getUsername(){
        return username;
    }

}
