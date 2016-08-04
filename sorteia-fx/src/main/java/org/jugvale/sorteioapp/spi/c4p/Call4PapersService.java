/**
 *
 */
package org.jugvale.sorteioapp.spi.c4p;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.ws.rs.core.GenericType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jugvale.sorteioapp.service.SorteioAppService;
import org.jugvale.sorteioapp.spi.c4p.impl.Inscricao;

/**
 * @author pedro-hos
 */
public class Call4PapersService implements SorteioAppService {

	private static final String ID_EVENTO = "evento_id";
	private final String BASE_URL = "http://call4papers-jugvale.rhcloud.com/rest/evento/";
	private final String MEDIA_TYPE = "application/json";
	private final String C4P_PROPS = "/c4p.properties";
	private final ResteasyClient client;
	private String eventoID = "312";
	
	private Properties c4pProps;
	public Call4PapersService() {
		c4pProps = new Properties();
		try {
			c4pProps.load(getClass().getResourceAsStream(C4P_PROPS));
			eventoID = c4pProps.getProperty(ID_EVENTO);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problema carregando arquivo " + C4P_PROPS + ". Usando valores padrões.");
		}		
		this.client = new ResteasyClientBuilder().build();
	}
	
	@Override
	public List<Object> buscaValores() {
		// pra evitar mexer no código do pedrão que já tá funfando ahushusa
		List<Object> objetos = new ArrayList<>();
		 pegaInscritosPresentesNoEvento(eventoID).forEach(objetos::add);
		 return objetos;
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
