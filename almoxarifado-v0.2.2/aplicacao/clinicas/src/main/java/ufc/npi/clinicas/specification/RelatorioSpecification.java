package ufc.npi.clinicas.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import ufc.npi.clinicas.model.AlocacaoItemSetor;
import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;

public final class RelatorioSpecification {
	public static Specification<Entrada> buscarEntradaPeriodoSetor( Date dataInicio, Date dataFim, Setor setor){
		return new Specification<Entrada>() {
			public Predicate toPredicate(Root<Entrada> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.and();
				if(dataFim!=null){
					predicate = builder.and(predicate, builder.lessThanOrEqualTo( root.get("chegada"), dataFim) );
				}
				
				if(dataInicio !=null){
					predicate = builder.and(predicate, builder.greaterThanOrEqualTo( root.get("chegada"), dataInicio) );
				}
				
				if(setor!=null){
					Subquery<Entrada> entradaQuery = query.subquery(Entrada.class);
					Root<AlocacaoItemSetor> itemEntrada =  entradaQuery.from(AlocacaoItemSetor.class);
					Join<ItemEntrada, Entrada> itensEntrada = itemEntrada.join("itemEntrada").join("entrada");
					entradaQuery.select(itensEntrada).where(builder.equal(itemEntrada.get("setor"),setor));
					predicate = builder.and(predicate, builder.in(root).value(entradaQuery));
				}
				
				return predicate;
			}
		};
	
	}
	
	public static Specification<SaidaMaterial> buscarSaidaPeriodoOrigem( Date dataInicio, Date dataFim, Setor setor){
		return new Specification<SaidaMaterial>() {
			public Predicate toPredicate(Root<SaidaMaterial> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.and();
				if(dataFim !=null){
					predicate = builder.and(predicate, builder.lessThanOrEqualTo( root.get("data"), dataFim) );
				}
				
				if(dataInicio !=null){
					predicate = builder.and(predicate, builder.greaterThanOrEqualTo( root.get("data"), dataInicio) );
				}
				
				if(setor!=null){
					predicate = builder.and(predicate, builder.equal(root.get("origem"), setor));
				}
				
				return predicate;
			}
		};
	
	}
	
}
