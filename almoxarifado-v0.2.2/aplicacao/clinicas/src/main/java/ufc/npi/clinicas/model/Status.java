package ufc.npi.clinicas.model;

public enum Status {
	
	FINALIZADA("FINALIZADA"), EM_ANDAMENTO("EM ANDAMENTO");
	
	private String descricao;

	private Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
