package models;


import java.util.HashMap;
import java.util.Map;

import play.libs.Json;
import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.util.observer.IObserver;

public class GridObserver implements IObserver {
	
	private Out<String> out;
	private IFiveWinsController controller;


	public GridObserver(IFiveWinsController controller,WebSocket.Out<String> out) {
		controller.addObserver(this);
		this.controller = controller;
		this.out = out;
	}

	@Override
	public void update() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("field", controller.getFieldString());
		out.write(controller.getFieldString());

	}
}
