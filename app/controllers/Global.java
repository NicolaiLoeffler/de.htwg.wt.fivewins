package controllers;

import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;
import play.Application;
import play.GlobalSettings;
import play.Play;

//import play.mvc.Http.RequestHeader;
public class Global extends GlobalSettings {
	/*
	 * @Override public Result onError(final RequestHeader requestHeader, final
	 * Throwable t) { return
	 * play.mvc.Controller.internalServerError(views.html.error500.render()); }
	 */
	@Override
	public void onStart(final Application app) {

		final String baseUrl = Play.application().configuration()
				.getString("baseUrl");
		final String googleKey = Play.application().configuration()
				.getString("google2Key");
		final String googleSecret = Play.application().configuration()
				.getString("google2Secret");

		System.out.println("Global Settings called");
		// Google OAuth 2.0
		final Google2Client google2Client = new Google2Client(googleKey,
				googleSecret);
		System.out.println(googleKey+" key\n"+googleSecret+ " secret");

		// Clients
		final Clients clients = new Clients(baseUrl + "/callback",
				google2Client);

		Config.setClients(clients);
		// for test purposes : profile timeout = 60 seconds
		// Config.setProfileTimeout(60);
	}
}