package controllers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.GridObserver;
import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.controller.IFiveWinsController;
import de.htwg.fivewins.field.Field;
import de.htwg.fivewins.field.VerySillyAI;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.Config;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

public class Application extends JavaController {

	static IFiveWinsController controller;
	private static String player_on_turn_sign = null;

	public static Result index() throws TechnicalException {
		final CommonProfile profile = getUserProfile();
		final String urlGoogle = getRedirectAction("OidcClient", "/game")
					.getLocation();


		return ok(views.html.index.render());
	}

	public static Result game() {
		return ok(views.html.game.render());
	}

	public static Result newGame(Integer fieldSize) {
		controller = new FiveWinsController(new Field(new Integer(fieldSize)));
		return ok();
	}

	public static Result newGameAI(Integer fieldSize, String sign) {
		Field field = new Field(fieldSize);
		controller = new FiveWinsController(field, new VerySillyAI("O", field));
		return ok();
	}

	public static Result setCell(String column, String row) {
		int col = new Integer(column) + 1;
		int r = new Integer(row) + 1;
		controller.handleInputOrQuit(col + "," + r);
		return ok();
	}

	public static Result gamefieldTojson() {
		return ok(Json.stringify(Json.toJson(controller.getField())));
	}

	public static WebSocket<String> connectWebSocket() {
		return new WebSocket<String>() {
			public void onReady(WebSocket.In<String> in,
					WebSocket.Out<String> out) {
				new GridObserver(controller, out);
			}

		};
	}

	private static Result protectedIndex() {
		// profile
		final CommonProfile profile = getUserProfile();
		return ok(views.html.protectedIndex.render(profile));
	}

	@RequiresAuthentication(clientName = "OidcClient")
	public static Result googleIndex() {
		return protectedIndex();
	}
}
