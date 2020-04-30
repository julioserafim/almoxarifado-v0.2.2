package ufc.npi.clinicas.util.alert;

public enum Type {
	INFO("INFORMAÇÃO"),
	SUCCESS("SUCESSO"),
	WARNING("AVISO"),
	ERROR("ERRO");	
	
	private String descricao;

	private Type(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
	
}
