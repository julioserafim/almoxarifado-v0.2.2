package ufc.npi.clinicas.model;

public enum StatusEntrada {
	FINALIZADA("FINALIZADA"), PENDENTE_INCLUSAO("PENDENTE DE INCLUSÃO DE MATERIAL"), PENDENTE_ALOCACAO("PENDENTE DE ALOCAÇÃO DE MATERIAL");
	
	private String descricao;

	private StatusEntrada(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
}
