package controllers;

import models.GridObserver;
import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.field.Field;
import de.htwg.fivewins.field.VerySillyAI;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;



public class Application extends Controller {

	static IFiveWinsController controller;
	
	/**
	 * Returns index page
	 */
    public static Result index() {
        return ok(views.html.index.render());
    }
    
    /**
     * Returns about page
     */
    public static Result about() {
        return ok(views.html.about.render());
    }
    
    /**
     * Starts new Game. Player vs. Player.
     * @param fieldSize
     * @return
     */
    public static Result newGame(Integer fieldSize) {
    	controller = new FiveWinsController(new Field(new Integer(fieldSize)));
    	return ok();
    }
    
    /**
     * Starts new Game vs. NPC.
     * @param fieldSize
     * @param sign
     * @return
     */
    public static Result newGameAI(Integer fieldSize, String sign) {
    	Field field = new Field(fieldSize);
    	controller = new FiveWinsController(field, new VerySillyAI("O", field));
    	return ok();
    }

    /**
     * Sets cell column/row.
     * @param column
     * @param row
     * @return 
     */
    public static Result setCell(String column, String row){	
    	int col = new Integer(column) +1;
    	int r = new Integer(row) +1;
        controller.handleInputOrQuit(col+","+r );
        return ok();
    }
    
    /**
     * Returns gamefield array.
     * @return
     */
    public static Result gamefieldTojson() {
         return ok(Json.stringify(Json.toJson(controller.getField())));
    }
    
    /**
     * @return Websocket
     */
    public static WebSocket<String> connectWebSocket() {
        return new WebSocket<String>() {      
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
            	new GridObserver(controller,out);
            }

        };
    }
}
