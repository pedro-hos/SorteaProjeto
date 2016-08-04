package org.jugvale.sorteioapp.spi.c4p.impl;

import javax.xml.bind.annotation.XmlRootElement;

import org.jugvale.sorteioapp.spi.c4p.model.DefaultModel;

@XmlRootElement
@SuppressWarnings("serial")
public class Inscricao extends DefaultModel {

	private Evento evento;
	private Participante participante;
	private boolean compareceu;
	private String data;

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public boolean isCompareceu() {
		return compareceu;
	}

	public void setCompareceu(boolean compareceu) {
		this.compareceu = compareceu;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return this.participante.getNome();
	}

}
