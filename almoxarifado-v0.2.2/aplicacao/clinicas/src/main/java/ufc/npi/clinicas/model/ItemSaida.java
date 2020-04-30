package ufc.npi.clinicas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemSaida {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer quantidade;
	
	private String lote;
	
	@ManyToOne
	@JsonIgnore
	private SaidaMaterial saidaMaterial;
	
	@ManyToOne
	private Material material;
	
	private Long qtdEmEstoque;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public SaidaMaterial getSaidaMaterial() {
		return saidaMaterial;
	}

	public void setSaidaMaterial(SaidaMaterial saidaMaterial) {
		this.saidaMaterial = saidaMaterial;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
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
		ItemSaida other = (ItemSaida) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getQtdEmEstoque() {
		return qtdEmEstoque;
	}

	public void setQtdEmEstoque(Long qtdEmEstoque) {
		this.qtdEmEstoque = qtdEmEstoque;
	}
	
	
	

}
