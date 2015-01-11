package models;

import de.htwg.fivewins.controller.IFiveWinsController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import play.libs.Json;
import play.mvc.WebSocket.Out;

public class GameInstance {
	private String player1;
	private String player2;
	private IFiveWinsController controller;
	public final UUID gameUUID;
	private Out<String> outPlayer1;

	public GameInstance(String name, IFiveWinsController controller) {
		this.player1 = name;
		this.player2 = null;
		this.controller = controller;
		this.gameUUID = UUID.randomUUID();
	}

	public void setPlayer2(String name) {
		this.player2 = name;
		Map<String, String> map = new HashMap<String, String>();
		map.put("started", "true");
		System.out.println("sending msg to player 1");
		outPlayer1.write(Json.stringify(Json.toJson(map)));
	}

	public String getPlayer1() {
		return this.player1;
	}

	public String getPlayer2() {
		return this.player2;
	}

	public UUID getGameId() {
		return this.gameUUID;
	}

	public IFiveWinsController getController() {
		return this.controller;
	}

	
	public void setOut(Out<String> out) {
		if (outPlayer1 == null) {
			outPlayer1 = out;
		}
	}

}