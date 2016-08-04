package org.jugvale.sorteioapp.spi.c4p.impl;

import javax.xml.bind.annotation.XmlRootElement;

import org.jugvale.sorteioapp.spi.c4p.model.DefaultModel;

@XmlRootElement
@SuppressWarnings("serial")
public class Evento extends DefaultModel {

	private String nome;
	private String descricao;
	private String dataInicio;
	private String dataFim;
	private String local;
	private String url;
	private boolean aceitandoTrabalhos;
	private boolean inscricoesAbertas;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAceitandoTrabalhos() {
		return aceitandoTrabalhos;
	}

	public void setAceitandoTrabalhos(boolean aceitandoTrabalhos) {
		this.aceitandoTrabalhos = aceitandoTrabalhos;
	}

	public boolean isInscricoesAbertas() {
		return inscricoesAbertas;
	}

	public void setInscricoesAbertas(boolean inscricoesAbertas) {
		this.inscricoesAbertas = inscricoesAbertas;
	}

}