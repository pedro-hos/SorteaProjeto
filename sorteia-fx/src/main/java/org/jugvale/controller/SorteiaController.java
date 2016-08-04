package org.jugvale.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.jugvale.model.impl.Inscricao;
import org.jugvale.service.ParticipanteService;

public class SorteiaController {

	public List<Object> paraSortear;
	public List<String> todasSorteadas = new ArrayList<>();
	public int MAX = 3;

	private static SorteiaController instance = null;

	public static SorteiaController getInstance() {

		if (Objects.isNull(instance)) {
			return new SorteiaController();
		} else {
			return instance;
		}
	}

	public void coletaDadosSorteio(final boolean numerico) {

		if (!numerico) {
			paraSortear = coletaParticipantes();
		} else {
			paraSortear = geraListaNumericaSorteio(MAX);
		}

	}

	private static List<Object> geraListaNumericaSorteio(final long max) {
		return LongStream.rangeClosed(1, max).mapToObj(String::valueOf).collect(Collectors.toList());
	}

	public String coletaSorteados() {
		return todasSorteadas.stream().collect(Collectors.joining(",", "Sorteados: ", ""));
	}

	private static List<Object> coletaParticipantes() {
		final Collection<Inscricao> inscritos = ParticipanteService.getInstance().pegaInscritosPresentesNoEvento("312");
		return inscritos.stream().map(i -> i.getParticipante().getNome()).collect(Collectors.toList());
	}

}
