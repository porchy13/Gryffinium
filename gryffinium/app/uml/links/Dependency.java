package uml.links;

import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;
import uml.entities.Entity;

public class Dependency
{
    private Integer id;
    private String name;

    private Entity from;
    private Entity to;

    public Dependency(Entity from, Entity to, String name)
    {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public Dependency(Entity from, Entity to){
        this(from, to, "");
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public ArrayNode getCreationCommands(){
        //TODO  implement
        return Json.newArray();
    }
}
