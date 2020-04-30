package ufc.npi.clinicas.model;

public enum TipoEntrada {
	FORNECEDOR("FORNECEDOR"), DOACAO("DOAÇÃO"),
	AJUSTE_ESTOQUE("AJUSTE DE ESTOQUE"), DEVOLUCAO("DEVOLUÇÃO"), OUTROS("OUTROS");
	
	private String descricao;

	private TipoEntrada(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
}
