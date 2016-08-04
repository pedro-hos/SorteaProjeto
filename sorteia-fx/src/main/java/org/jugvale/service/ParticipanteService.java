/**
 *
 */
package org.jugvale.service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.core.GenericType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jugvale.model.impl.Inscricao;

/**
 * @author pedro-hos
 */
public class ParticipanteService {

	private final String BASE_URL = "http://call4papers-jugvale.rhcloud.com/rest/evento/";
	private final String MEDIA_TYPE = "application/json";
	private final ResteasyClient client;

	private final static ParticipanteService instance = null;

	public ParticipanteService() {
		this.client = new ResteasyClientBuilder().build();
	}

	public static ParticipanteService getInstance() {
		if (Objects.isNull(instance)) {
			return new ParticipanteService();
		} else {
			return instance;
		}
	}

	public Collection<Inscricao> pegaInscritosPresentesNoEvento(final String eventoId) {
		final Collection<Inscricao> inscritos = pegaInscritosNoEvento(eventoId);
		return inscritos.stream().filter(Inscricao::isCompareceu).collect(Collectors.toList());
	}

	public Collection<Inscricao> pegaInscritosNoEvento(final String eventoId) {
		return client.target(BASE_URL).path(eventoId).path("inscritos").request(MEDIA_TYPE)
				.get(new GenericType<Collection<Inscricao>>() {
				});

	}

}
