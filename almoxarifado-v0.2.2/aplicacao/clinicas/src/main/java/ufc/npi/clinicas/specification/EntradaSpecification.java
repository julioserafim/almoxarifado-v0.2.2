package ufc.npi.clinicas.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;


import org.springframework.data.jpa.domain.Specification;
import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Material;

public final class EntradaSpecification {
	
	public static Specification<Entrada> buscarEntradaPeriodoMaterial( Date dataInicio, Date dataFim, Material material ){
		return new Specification<Entrada>() {
			public Predicate toPredicate(Root<Entrada> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.and();
				if(dataInicio !=null){
					predicate = builder.and(predicate, builder.lessThanOrEqualTo( root.get("chegada"), dataInicio) );
				}
				
				if(dataFim !=null){
					predicate = builder.and(predicate, builder.greaterThanOrEqualTo( root.get("chegada"), dataFim) );
				}
				
				if(material!=null){
					Subquery<Entrada> entradaQuery = query.subquery(Entrada.class);
					Root<ItemEntrada> itemEntrada =  entradaQuery.from(ItemEntrada.class);
					Join<ItemEntrada, Entrada> itensEntrada = itemEntrada.join("entrada");
					entradaQuery.select(itensEntrada).where(builder.equal(itemEntrada.get("material"),material));
					predicate = builder.and(predicate, builder.in(root).value(entradaQuery));
				}
				
				return predicate;
			}
		};
	}

}
