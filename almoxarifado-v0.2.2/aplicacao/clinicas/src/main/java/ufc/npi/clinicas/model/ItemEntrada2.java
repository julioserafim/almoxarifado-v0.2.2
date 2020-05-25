package ufc.npi.clinicas.model;


import java.math.BigDecimal;

public class ItemEntrada2 {
	private Integer quantidade;
	private BigDecimal valorUnitario;

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorTotal() {
		if (valorUnitario == null)
			return BigDecimal.ZERO;
		return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
	}
}