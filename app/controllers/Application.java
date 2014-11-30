package controllers;


import java.util.HashMap;
import java.util.Map;

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
    	/* 
    	 * controller.getPlayerSign() hast to be called before handleInputOrQuit
    	 * because after handleOrInput turn is already switchted to next Player
    	 */   	
    	player_on_turn_sign = controller.getPlayerSign();
    	controller.handleInputOrQuit(column+","+row);
    	return json();
    }
    
    public static Result json() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("isDraw", Boolean.toString(controller.getDraw()));
        map.put("playerSign", player_on_turn_sign);
        map.put("status", controller.getStatus());      
        map.put("isWon", Boolean.toString(controller.getWinner()));
        map.put("winner", controller.getWinnerSign());

        return ok(Json.stringify(Json.toJson(map)));
    }
    
    public static WebSocket<String> connectWebSocket() {
        return new WebSocket<String>() {      
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
            	new GridObserver(controller,out);
            }

        };
    }
}
