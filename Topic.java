/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dzunani
 */
public class Topic {
    private int id;
    private String title;
    private String description;
    private String username;  // The name of the user who created the topic

    // Default constructor
    public Topic() {
    }

    // Parameterized constructor
    public Topic(int id, String title, String description, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.username = username;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Optional: Override toString() for easy debugging
    @Override
    public String toString() {
        return "Topic [id=" + id + ", title=" + title + ", description=" + description + ", username=" + username + "]";
    }
}
