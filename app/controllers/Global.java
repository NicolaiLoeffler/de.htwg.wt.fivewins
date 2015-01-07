package controllers;

import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.oidc.client.OidcClient;
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
		
		 // OpenID Connect
		final OidcClient oidcClient = new OidcClient();
		oidcClient.setClientID("747624574509-i6nesa9okgcdaiuennf9qfcp2m69long.apps.googleusercontent.com");
		oidcClient.setSecret("k5o6-YrlYXTH-u4MLizMEUBd");
		oidcClient.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration");
		oidcClient.addCustomParam("prompt", "consent");
		
		// Clients
		final Clients clients = new Clients(baseUrl + "/callback",
				oidcClient);
		

		Config.setClients(clients);
		// for test purposes : profile timeout = 60 seconds
		// Config.setProfileTimeout(60);
	}
}