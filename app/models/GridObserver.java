package models;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import play.libs.Json;
import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;
import de.htwg.fivewins.controller.game.IFiveWinsController;
import de.htwg.util.observer.IObserver;

public class GridObserver implements IObserver {
	
	private Out<String> out;
	private IFiveWinsController controller;
	private String gameUUID;

	/**
	 * Constructor
	 * @param controller
	 * @param out
	 */
	public GridObserver(IFiveWinsController controller,WebSocket.Out<String> out, String gameUUID) {
		controller.addObserver(this);
		this.controller = controller;
		this.out = out;
		this.gameUUID = gameUUID;
	}

	/**
	 * Override method from IObserver. Sends changed data to game.
	 */
	@Override
	public void update() {
		Map<String, String> map = new HashMap<String, String>();
        // all informations
        map.put("isDraw", Boolean.toString(controller.getDraw()));
        map.put("playerSign", controller.getPlayerSign());
        map.put("status", controller.getStatus());      
        map.put("isWon", Boolean.toString(controller.getWinner()));
        map.put("winner", controller.getWinnerSign());
        // TODO: controller.getPlayerLeft() 
        map.put("playerLeft", Boolean.toString(false));
        map.put("gameUUID", this.gameUUID);
      	
		//preparation of gamefield
		String[][] gameField = controller.getField();
		// hardcopy of gamefield
		String[][] tmpGameField = new String[gameField.length][gameField.length];
		for(int i = 0; i < gameField.length; i++)
		    tmpGameField[i] = gameField[i].clone();
		
		//replace "-" to " "
		for(int i = 0; i < gameField.length; i++) {
			for(int j = 0; j < gameField.length; j++) {
				if("-".equals(gameField[i][j])) {
					tmpGameField[i][j] = " ";
				}
			}
		}
		
        map.put("gameField", Json.stringify(Json.toJson(tmpGameField)));
		
        out.write(Json.stringify(Json.toJson(map)));
	}
}
