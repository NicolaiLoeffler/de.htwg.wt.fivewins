package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import models.GridObserver;
import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.field.Field;
import de.htwg.fivewins.field.VerySillyAI;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import models.GameInstance;

public class Application extends Controller {

	static IFiveWinsController controller;
	static Map<UUID, GameInstance> gameInstances = new HashMap<UUID, GameInstance>();

	/**
	 * Returns index page
	 */
	public static Result index() {
		session("connected", "");
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
	 * 
	 * @param fieldSize
	 * @return
	 */
	public static Result newGame(Integer fieldSize) {
		session("gameId","local");
		controller = new FiveWinsController(new Field(new Integer(fieldSize)));
		return ok();
	}

	/**
	 * Starts new Game vs. NPC.
	 * 
	 * @param fieldSize
	 * @param sign
	 * @return
	 */
	public static Result newGameAI(Integer fieldSize, String sign) {
		session("gameId","local");
		Field field = new Field(fieldSize);
		controller = new FiveWinsController(field, new VerySillyAI("O", field));
		return ok();
	}

	/**
	 * Sets cell column/row.
	 * 
	 * @param column
	 * @param row
	 * @return
	 */
	public static Result setCell(String column, String row) {
		int col = new Integer(column) + 1;
		int r = new Integer(row) + 1;
		final String gameUUID = session("gameId");
		if (gameUUID.equals("local")) {
			controller.handleInputOrQuit(col + "," + r);
		} else {
			IFiveWinsController c = gameInstances.get(UUID.fromString(gameUUID)).getController();
			if(c == null){
				System.out.println("Couldnt get Instance controller for game instance "+gameUUID);
				return ok();
			}
			c.handleInputOrQuit(col + "," + r);
			System.out.println("set cell online");
		}
		return ok();
	}

	/**
	 * Returns gamefield array.
	 * 
	 * @return
	 */
	public static Result gamefieldTojson() {
		return ok(Json.stringify(Json.toJson(controller.getField())));
	}

	/**
	 * @return Websocket
	 */
	public static WebSocket<String> connectWebSocket() {
		final String gameUUID = session("gameId");
		/*if(gameUUID == null){
			System.out.println("Is null");
		}
		if(gameUUID.equals("")){
			System.out.println("empty String");
		}*/
		return new WebSocket<String>() {
			public void onReady(WebSocket.In<String> in,
					WebSocket.Out<String> out) {
				if (gameUUID.equals("local")) {
					new GridObserver(controller, out);					
				} else {
					System.out.println("starting grid observer for"+gameUUID);
					new GridObserver(gameInstances.get(
							UUID.fromString(gameUUID)).getController(), out);
				}
			}
		};
	}

	public static Result joinOnlineGame() {
		GameInstance gameInstance = getJoinableGame();
		if (gameInstance == null) {
			gameInstance = new GameInstance("X", new FiveWinsController(
					new Field(8)));
			gameInstances.put(gameInstance.gameUUID, gameInstance);
		} else {
			gameInstance.setPlayer2("O");
		}
		session("gameId", gameInstance.gameUUID + "");
		System.out.println(session("gameId"));
		for (UUID id : gameInstances.keySet()) {
			System.out.println("Game Instance " + id);
		}
		return ok();
	}

	public static GameInstance getJoinableGame() {
		for (UUID id : gameInstances.keySet()) {
			GameInstance instance = gameInstances.get(id);
			if (instance.getPlayer1() == "X" && instance.getPlayer2() == null) {
				System.out.println("Joinable Instance found");
				return instance;
			}
		}
		return null;
	}

	private static GameInstance getGameInstance() {
		UUID gameId = UUID.fromString(session("gameId"));
		return gameInstances.get(gameId);
	}
}
