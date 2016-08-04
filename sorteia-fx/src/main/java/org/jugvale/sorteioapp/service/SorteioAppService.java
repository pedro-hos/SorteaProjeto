package org.jugvale.sorteioapp.service;

import java.util.List;
import java.util.Objects;

import org.jugvale.sorteioapp.spi.c4p.Call4PapersService;

/**
 * 
 * Contrato para buscar valores para o sorteio.
 * 
 * @author wsiqueir
 *
 */
public interface SorteioAppService {
	
	
	
	public List<Object> buscaValores();
	
	/**
	 * 
	 * Factory para essa interface. Implementar chamada a uma implementação aqui.
	 * 
	 * @author wsiqueir
	 *
	 */
	public class Factory {
	
		private static SorteioAppService INSTANCE;
		
		public static SorteioAppService get() {
			if(Objects.isNull(INSTANCE)) {
				
				// TODO: chamada a um SPI manualmente
				// deveria ser configurável para que usuários pudessem plugar os seus provedores de valores para sorteio
				INSTANCE = new Call4PapersService();
			}
			return INSTANCE;
		}
	}
	
}
