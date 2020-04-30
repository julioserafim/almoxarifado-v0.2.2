package ufc.npi.clinicas.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nome" , "unidade_medida_id"})})
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String nome;

	@Column
	private String codigoInterno;

	@Column
	@Type(type="text")
	private String descricao;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, mappedBy = "material")
	@JsonManagedReference
	private List<CodigoDeBarras> codigos;

	private Integer estoque;
	
	private Integer estoqueMinimo;

	@ManyToOne
	private UnidadeMedida unidadeMedida;

	@ManyToOne
	private GrupoMaterial grupo;

	public List<CodigoDeBarras> getCodigos() {
		return codigos;
	}

	public boolean addCodigo(CodigoDeBarras codigo) {
		if (codigos == null) {
			codigos = new ArrayList<>();
		} else if (codigos.contains(codigo)) {
			return false;
		}
		return codigos.add(codigo);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public Integer getEstoque() {
		return estoque;
	}

	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}
	
	public Integer getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Integer estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}
	

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public GrupoMaterial getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoMaterial grupo) {
		this.grupo = grupo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoInterno == null) ? 0 : codigoInterno.hashCode());
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
		Material other = (Material) obj;
		if (codigoInterno == null) {
			if (other.codigoInterno != null)
				return false;
		} else if (!codigoInterno.equals(other.codigoInterno))
			return false;
		return true;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public void removeCodigo(CodigoDeBarras codigoDeBarras) {
		if (codigoDeBarras != null){
			codigos.remove(codigoDeBarras);
		}
	}
}