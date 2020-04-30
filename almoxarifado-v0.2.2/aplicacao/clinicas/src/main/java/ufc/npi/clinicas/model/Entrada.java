package ufc.npi.clinicas.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Entrada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String pregao;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Past(message="A data de chegada não pode ser posterior ao dia de hoje.")
	private Date chegada;
	
	private String empenho;
	
	private String notaFiscal;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date emissaoNotaFiscal;
	
	@Min(value=0, message="O valor da nota fiscal não deve ser menor que 0.")
	private BigDecimal valorNotaFiscal;
	
	private String processoLicitacao;
	
	private String licitacao;
	
	private String observacao;

	@Enumerated(EnumType.STRING)
	private StatusEntrada statusEntrada;
	
	@Enumerated(EnumType.STRING)
	private TipoEntrada tipo;
	
	@ManyToOne
	private Fornecedor fornecedor;
	
	@ManyToOne
	private Usuario responsavel;
	
	@ManyToOne
	private Setor setor;

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy="entrada")
	private List<ItemEntrada> itens;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPregao() {
		return pregao;
	}

	public void setPregao(String pregao) {
		this.pregao = pregao;
	}

	public Date getChegada() {
		return chegada;
	}

	public void setChegada(Date chegada) {
		this.chegada = chegada;
	}

	public String getEmpenho() {
		return empenho;
	}

	public void setEmpenho(String empenho) {
		this.empenho = empenho;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public Date getEmissaoNotaFiscal() {
		return emissaoNotaFiscal;
	}

	public void setEmissaoNotaFiscal(Date emissaoNotaFiscal) {
		this.emissaoNotaFiscal = emissaoNotaFiscal;
	}

	public BigDecimal getValorNotaFiscal() {
		return valorNotaFiscal;
	}

	public void setValorNotaFiscal(BigDecimal valorNotaFiscal) {
		this.valorNotaFiscal = valorNotaFiscal;
	}

	public String getProcessoLicitacao() {
		return processoLicitacao;
	}

	public void setProcessoLicitacao(String processoLicitacao) {
		this.processoLicitacao = processoLicitacao;
	}

	public String getLicitacao() {
		return licitacao;
	}

	public void setLicitacao(String licitacao) {
		this.licitacao = licitacao;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public StatusEntrada getStatusEntrada() {
		return statusEntrada;
	}

	public void setStatusEntrada(StatusEntrada statusEntrada) {
		this.statusEntrada = statusEntrada;
	}

	public TipoEntrada getTipo() {
		return tipo;
	}

	public void setTipo(TipoEntrada tipo) {
		this.tipo = tipo;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public List<ItemEntrada> getItens() {
		//ordena itens por ordem alfabetica
		Collections.sort(itens,new Comparator<ItemEntrada>() {
		    @Override
		    public int compare(ItemEntrada a, ItemEntrada b) {
		        return a.getMaterial().getNome().compareTo(b.getMaterial().getNome());
		    }
		});
		return this.itens;
	}

	public void addItem(ItemEntrada item) {
		if (itens == null) {
			itens = new ArrayList<ItemEntrada>();
		}
		this.itens.add(item);
	}
	
	public void removeItem(ItemEntrada item) {
		if (itens != null) {
			itens.remove(item);
		}
	}
	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	public void setItens(List<ItemEntrada> itens) {
		this.itens = itens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((notaFiscal == null) ? 0 : notaFiscal.hashCode());
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
		Entrada other = (Entrada) obj;
		if (notaFiscal == null) {
			if (other.notaFiscal != null)
				return false;
		} else if (!notaFiscal.equals(other.notaFiscal))
			return false;
		return true;
	}
	
	public BigDecimal somaDosItens(){
		BigDecimal soma = BigDecimal.ZERO;
		for(ItemEntrada item : itens){
			soma = soma.add(item.getValorTotal());
		}
		return soma;
	}
}
