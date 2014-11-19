package controllers;


import java.util.HashMap;
import java.util.Map;

import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.field.Field;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
    
	static IFiveWinsController controller;
	
    public static Result index() {
        return ok(views.html.index.render());
    }
    
    public static Result game() {
        return ok(views.html.game.render());
    }
    
    public static Result newGame(Integer fieldSize) {
    	controller = new FiveWinsController(new Field(new Integer(fieldSize)));
    	String fieldString = controller.getFieldString();
    	return ok(views.html.field.render(fieldString, fieldSize));
    }

    public static Result setCell(String column, String row){
    	controller.handleInputOrQuit(column+","+row);
    	return json();
    }
    
    public static Result json() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isDraw", Boolean.toString(controller.getDraw()));
        map.put("playerSign", controller.getPlayerSign());
        map.put("status", controller.getStatus());
        map.put("isWon", Boolean.toString(controller.getWinner()));
        map.put("winner", controller.getWinnerSign());

        return ok(Json.stringify(Json.toJson(map)));
    }    
}
