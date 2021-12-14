package ru.iliavolkov.notes.ui;

public class NotesClass {

    private String title;
    private String description;
    private String date;

    public NotesClass(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

}
