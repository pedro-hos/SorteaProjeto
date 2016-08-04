package org.jugvale.sorteioapp.spi.c4p.impl;

import org.jugvale.sorteioapp.spi.c4p.model.DefaultModel;

public class Participante extends DefaultModel {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String email;
	private String rg;
	private String empresa;
	private String instituicao;
	private Nivel nivel;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
}
