/**
 * 
 */
package org.jugvale.service;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * @author pedro-hos
 */
public class ParticipanteService {

	private final String BASE_URL = "http://call4papers-jugvale.rhcloud.com/rest/evento/";
	private final String MEDIA_TYPE = "application/json";
	private ResteasyClient client;

	public ParticipanteService() {
		this.client = new ResteasyClientBuilder().build();
	}

	public void pegaInscritosNoEvento(final String eventoId) {
		String string = client.target(BASE_URL).path(eventoId).path("inscritos").request(MEDIA_TYPE).get(String.class);
		System.out.println(string);
	}

}
