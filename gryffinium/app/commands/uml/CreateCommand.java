package commands.uml;

import akka.dispatch.sysmsg.Create;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import dto.entities.*;
import dto.entities.operations.MethodDto;
import dto.entities.operations.OperationDto;
import dto.links.*;
import dto.entities.variables.*;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.operations.Operation;
import uml.entities.variables.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.links.*;

public class CreateCommand implements Command
{
    JsonNode data;

    dto.ElementTypeDto elementType;

    public CreateCommand(JsonNode data, dto.ElementTypeDto elementType)
    {
        this.data = data;
        this.elementType = elementType;
    }

    public void setElement(JsonNode data)
    {
        this.data = data;
    }

    public JsonNode getElement()
    {
        return data;
    }

    @Override
    public ArrayNode execute(Project project)
    {
        ArrayNode result = Json.newArray();

        switch (elementType)
        {
            case CLASS:
                Class c = new Class(Json.fromJson(data, ClassDto.class), project.getDiagram());
                project.getDiagram().addEntity(c);
                result.add(Command.createResponse(c.toDto(), elementType));
                break;
            case INNER_CLASS:
                InnerClass ic = new InnerClass(
                        Json.fromJson(data, InnerClassDto.class),
                        project.getDiagram());

                project.getDiagram().addEntity(ic);
                result.add(Command.createResponse(ic.toDto(), elementType));
                break;
            case ASSOCIATION_CLASS:
                AssociationClass ac = new AssociationClass(
                        Json.fromJson(data, AssociationClassDto.class),
                        project.getDiagram());

                project.getDiagram().addEntity(ac);
                result.add(Command.createResponse(ac.toDto(), elementType));
                break;
            case ENUM:
                Enum e = new Enum(Json.fromJson(data, EnumDto.class), project.getDiagram());
                project.getDiagram().addEntity(e);
                result.add(Command.createResponse(e.toDto(), elementType));
                break;
            case INTERFACE:
                Interface i = new Interface(Json.fromJson(data,
                        EntityDto.class), project.getDiagram());
                project.getDiagram().addEntity(i);
                result.add(Command.createResponse(i.toDto(), elementType));
                break;
            case INNER_INTERFACE:
                InnerInterface ii = new InnerInterface(
                        Json.fromJson(data, InnerInterfaceDto.class),
                        project.getDiagram());
                project.getDiagram().addEntity(ii);
                result.add(Command.createResponse(ii.toDto(), elementType));
                break;


            case BINARY_ASSOCIATION:
                BinaryAssociation ba = new BinaryAssociation(
                        Json.fromJson(data, BinaryAssociationDto.class),
                        project.getDiagram());

                project.getDiagram().addAssociation(ba);
                result.add(Command.createResponse(ba.toDto(), elementType));
                break;
            case AGGREGATION:
                Aggregation ag = new Aggregation(
                        Json.fromJson(data, BinaryAssociationDto.class),
                        project.getDiagram());

                project.getDiagram().addAssociation(ag);
                result.add(Command.createResponse(ag.toDto(), elementType));
                break;
            case COMPOSITION:
                Composition co = new Composition(
                        Json.fromJson(data, BinaryAssociationDto.class),
                        project.getDiagram());
                project.getDiagram().addAssociation(co);

                result.add(Command.createResponse(co.toDto(), elementType));
                break;
            case MUTLI_ASSOCIATION:
                break;
            case DEPENDENCY:
                Dependency d = new Dependency(
                        Json.fromJson(data, DependencyDto.class),
                        project.getDiagram());

                project.getDiagram().addDependency(d);
                result.add(Command.createResponse(d.toDto(), elementType));
                break;
            case GENERALIZATION:
                Generalization g = new Generalization(
                        Json.fromJson(data, GeneralizationDto.class),
                        project.getDiagram());

                project.getDiagram().addRelationship(g);
                result.add(Command.createResponse(g.toDto(), elementType));
                break;
            case REALIZATION:
                Realization r = new Realization(
                        Json.fromJson(data, RealizationDto.class),
                        project.getDiagram());

                project.getDiagram().addRelationship(r);
                result.add(Command.createResponse(r.toDto(), elementType));
                break;
            case INNER:
                break;

            case VALUE:
                ValueDto gv = Json.fromJson(data,
                        ValueDto.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                // TODO : check name uniqueness
                gv.setValue("value" + eParent.getValues().size());
                eParent.addValue(gv.getValue());

                result.add(Command.createResponse(gv, elementType));
                break;

            case ATTRIBUTE:
                AttributeDto ga = Json.fromJson(data,
                        AttributeDto.class);
                Attribute a = new Attribute(ga, project.getDiagram());
                Entity attrParent =
                        project.getDiagram().getEntity(ga.getParentId());
                attrParent.addAttribute(a);

                result.add(Command.createResponse(a.toDto(attrParent),
                        elementType));
                break;
            case PARAMETER:
                ParameterDto gp = Json.fromJson(data,
                        ParameterDto.class);
                Parameter p = new Parameter(gp, project.getDiagram());
                Entity paramParent =
                        project.getDiagram().getEntity(gp.getParentId());
                Operation op = paramParent.getOperationById(gp.getMethodId());

                op.addParam(p);
                result.add(Command.createResponse(p.toDto(paramParent, op),
                        elementType));
                break;


            case CONSTRUCTOR:
                OperationDto go = Json.fromJson(data,
                        OperationDto.class);
                ConstructableEntity ctorParent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());

                Constructor ctor = new Constructor(go, project.getDiagram());
                ctorParent.addConstructor(ctor);

                result.add(Command.createResponse(ctor.toDto(ctorParent),
                        elementType));
                break;
            case METHOD:
                MethodDto gm = Json.fromJson(data,
                        MethodDto.class);
                Method m = new Method(gm, project.getDiagram());

                Entity methParent =
                        project.getDiagram().getEntity(gm.getParentId());
                methParent.addMethod(m);
                result.add(Command.createResponse(m.toDto(methParent),
                        elementType));
                break;
        }
        return result;
    }


}
