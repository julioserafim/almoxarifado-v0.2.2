package ufc.npi.clinicas.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemEntrada {
	
	private ItemEntrada2 itemEntrada2 = new ItemEntrada2();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String lote;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date validade;
	
	@ManyToOne	
	private Material material;
	
	@ManyToOne
	@JoinColumn
	@JsonIgnore
	private Entrada entrada;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="itemEntrada")
	@JsonIgnore
	private List<AlocacaoItemSetor> alocacoes;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	public Integer getQuantidade() {
		return itemEntrada2.getQuantidade();
	}

	public void setQuantidade(Integer quantidade) {
		itemEntrada2.setQuantidade(quantidade);
	}

	public BigDecimal getValorUnitario() {
		return itemEntrada2.getValorUnitario();
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		itemEntrada2.setValorUnitario(valorUnitario);
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public List<AlocacaoItemSetor> getAlocacoes() {
		return alocacoes;
	}
	
	public int quantidadeItensAlocados(){	
		return 0;
	}	
	
	public void setAlocacoes(List<AlocacaoItemSetor> alocacoes) {
		this.alocacoes = alocacoes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntrada other = (ItemEntrada) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public BigDecimal getValorTotal(){
		return itemEntrada2.getValorTotal();
	}
	
	public Integer totalItensAlocados(){
		Integer soma = 0;
		
		for(AlocacaoItemSetor itemSetor: this.getAlocacoes()){
			soma += itemSetor.getQuantidade();
		}
		
		return soma;
	}
	

}
