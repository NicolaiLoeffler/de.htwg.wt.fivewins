package controllers;


import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.GridObserver;
import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.field.Field;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

public class Application extends Controller {
    
	static IFiveWinsController controller;
	private static String player_on_turn_sign = null;
	
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
//    	if(column.equals("") || row.equals("")) {
//    		return badRequest();
//    	}
    	/* 
    	 * controller.getPlayerSign() hast to be called before handleInputOrQuit
    	 * because after handleOrInput turn is already switchted to next Player
    	 */
    	
    	int col = new Integer(column) +1;
    	int r = new Integer(row) +1;
    	
        controller.handleInputOrQuit(col+","+r );
    	//    	controller.handleInputOrQuit(column+","+row);
        Map<String, String> map = new HashMap<String, String>();
//        // all informations
        map.put("isDraw", Boolean.toString(controller.getDraw()));
        map.put("playerSign", controller.getPlayerSign());
        map.put("status", controller.getStatus());      
        map.put("isWon", Boolean.toString(controller.getWinner()));
        map.put("winner", controller.getWinnerSign());
      	// all informations
    	
//    		controller.handleInputOrQuit(column+","+row);

        
      	map.put("column", ""+col);
      	map.put("row", ""+r);
        return ok(Json.stringify(Json.toJson(map)));
    }
    
    public static Result gamefieldTojson() {
         return ok(Json.stringify(Json.toJson(controller.getField())));
    }
    
    public static WebSocket<String> connectWebSocket() {
        return new WebSocket<String>() {      
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
            	new GridObserver(controller,out);
            }

        };
    }
}
