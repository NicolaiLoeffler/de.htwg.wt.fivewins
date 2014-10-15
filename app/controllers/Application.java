package controllers;


import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.field.Field;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
    
	static IFiveWinsController controller;
	
    public static Result index() {
        return ok(views.html.index.render("Hello Play Framework",8));
    }
    
    public static Result newGame(Integer fieldSize) {
    	controller = new FiveWinsController(new Field(new Integer(fieldSize)));
    	String fieldString = controller.getFieldString();
    	return ok(views.html.index.render(fieldString, fieldSize));
    }

    public static Result setCell(String column, String row){
    	controller.handleInputOrQuit(column+","+row);
    	return ok(views.html.index.render(controller.getFieldString(),8));
    }
    
    
}
