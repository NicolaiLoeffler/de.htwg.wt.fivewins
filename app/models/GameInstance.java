package models;

import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import java.util.UUID;

public class GameInstance {
	private String player1;
	private String player2;
	private IFiveWinsController controller;
	public final UUID gameUUID;

	public GameInstance(String name, IFiveWinsController controller) {
		this.player1 = name;
		this.player2 = null;
		this.controller = controller;
		this.gameUUID = UUID.randomUUID();
	}

	public void setPlayer2(String name) {
		this.player2 = name;
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
}