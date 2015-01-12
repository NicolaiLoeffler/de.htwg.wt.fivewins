package controllers;

import java.util.HashMap;
import java.util.LinkedHashMap;
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


import com.fasterxml.jackson.databind.node.ObjectNode;

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

		return new WebSocket<String>() {
			public void onReady(WebSocket.In<String> in,
					WebSocket.Out<String> out) {
				if (gameUUID.equals("local")) {
					new GridObserver(controller, out, gameUUID);					
				} else {
					System.out.println("starting grid observer for"+gameUUID);
					gameInstances.get(
							UUID.fromString(gameUUID)).setOut(out);
					new GridObserver(gameInstances.get(
							UUID.fromString(gameUUID)).getController(), out, gameUUID);
				}
			}
		};
	}

	public static Result joinOnlineGame() {
		ObjectNode result = Json.newObject();
		String playerId;
		GameInstance gameInstance = getJoinableGame();
		if (gameInstance == null) {
			gameInstance = new GameInstance("X", new FiveWinsController(
					new Field(8)));
			gameInstances.put(gameInstance.gameUUID, gameInstance);
			playerId = "X";
			System.out.println(gameInstance.getGameId()+" player X joined");
		} else {
			gameInstance.setPlayer2("O");
			playerId = "O";
			System.out.println(gameInstance.getGameId()+" player O joined");
			System.out.println(gameInstance.getGameId()+" is ready to play");
		}
		session("gameId", gameInstance.gameUUID + "");
		session("playerId",playerId);
		result.put("playerId", playerId);
		result.put("gameUUID", gameInstance.gameUUID.toString());
		return ok(result);
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
	public static Result stopGame(){
		System.out.println("Stopping Game");
		IFiveWinsController c = gameInstances.get(UUID.fromString(session("gameId"))).getController();
		c.handleInputOrQuit("q");
		return ok();
	}
}
