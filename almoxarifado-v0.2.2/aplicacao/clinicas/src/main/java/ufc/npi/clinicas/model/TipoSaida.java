package ufc.npi.clinicas.model;

public enum TipoSaida {
	

	SETOR("SETOR"), SINISTRO("SINISTRO"), AJUSTE_DE_ESTOQUE("AJUSTE DE ESTOQUE"), 
	DOACAO("DOAÇÃO"), VENCIMENTO("VENCIMENTO"), OUTROS("OUTROS");

	
	private String descricao;

	private TipoSaida(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
