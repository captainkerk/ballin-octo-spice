package controllers;

import play.mvc.Controller;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import play.Logger;
import play.db.DB;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Application extends Controller {

    public static Result getUsers()
    {
        final String SELECT_ALL_USERS = "SELECT * FROM Users;";

        try(Connection connection = DB.getConnection())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);

            ResultSet resultSet = preparedStatement.executeQuery();

            ObjectNode usersJsonNode = Json.newObject();

            int position = 0;

            while (resultSet.next())
            {
                usersJsonNode.put(String.valueOf(position), resultSet.getString(2));
                position++;
            }

            ObjectNode jsonResultNode = Json.newObject();

            jsonResultNode.put("length", position);
            jsonResultNode.put("users", usersJsonNode);

            return ok(jsonResultNode);
        }
        catch (Exception e)
        {
            System.out.println(new DateTime().toDateTime().toString() + " -- Something went wrong connecting to DB");
        }
        return badRequest();

    }

    public static Result healthcheckOne()
    {
        return ok();

    }

    public static Result healthcheckTwo()
    {
        DateTime dateTime = new DateTime();

        if(dateTime.getMinuteOfDay() % 2 == 0)
        {
            return ok();
        }
        else
        {
            return badRequest();
        }
    }


}
