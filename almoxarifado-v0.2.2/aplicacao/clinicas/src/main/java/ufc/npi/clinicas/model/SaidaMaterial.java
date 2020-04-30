package ufc.npi.clinicas.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SaidaMaterial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private TipoSaida tipo;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Past(message="A data de chegada não pode ser posterior ao dia de hoje.")
	private Date data;

	private String observacao;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne
	private Setor origem;
	
	@ManyToOne
	private Setor destino;
	
	@ManyToOne
	private Usuario responsavel;
	
	@JsonIgnore
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="saidaMaterial")
	private List<ItemSaida> itens;

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Setor getOrigem() {
		return origem;
	}

	public void setOrigem(Setor origem) {
		this.origem = origem;
	}

	public Setor getDestino() {
		return destino;
	}

	public void setDestino(Setor destino) {
		this.destino = destino;
	}

	public void setItens(List<ItemSaida> itens) {
		this.itens = itens;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoSaida getTipo() {
		return tipo;
	}

	public void setTipo(TipoSaida tipo) {
		this.tipo = tipo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public List<ItemSaida> getItens() {
		return itens;
	}

	public void addItem(ItemSaida item) {
		if (this.itens == null) {
			this.itens = new ArrayList<ItemSaida>();
		}
		this.itens.add(item);
	}
	
	public void removeItem(ItemSaida item) {
		if (this.itens != null) {
			this.itens.remove(item);
		}
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
		SaidaMaterial other = (SaidaMaterial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
