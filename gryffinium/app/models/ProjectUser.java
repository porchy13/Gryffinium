package models;

import actors.UserActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import commands.ChatMessageCommand;
import commands.Command;
import io.ebean.Model;
import io.ebean.annotation.NotNull;
import play.libs.Json;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProjectUser extends Model {
    @ManyToOne(optional = false)
    public User user;

    @ManyToOne(optional = false)
    public Project project;

    @NotNull
    public boolean isOwner;

    @NotNull
    public boolean canRead;

    @NotNull
    public boolean canWrite;

    private UserActor actor;

    public ProjectUser(User user, Project project, boolean isOwner, boolean canRead, boolean canWrite) {
        this.user = user;
        this.project = project;
        this.isOwner = isOwner;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public void setActor(UserActor actor)
    {
        this.actor = actor;
    }

    public UserActor getActor()
    {
        return actor;
    }

    public User getUser() {
        return user;
    }

    public void handleMessage(JsonNode message)
    {
        Command cmd;
        switch(message.get("type").asText()){
            case "ChatMessage":

                cmd = Json.fromJson(message, ChatMessageCommand.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }

        System.out.println("project");
        System.out.println(project);
        project.executeCommand(cmd, this);
    }

    public void send(JsonNode message){
        actor.send(message);
    }

}
