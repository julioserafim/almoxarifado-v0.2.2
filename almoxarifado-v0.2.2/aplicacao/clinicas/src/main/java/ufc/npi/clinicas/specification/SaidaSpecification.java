package ufc.npi.clinicas.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;


import org.springframework.data.jpa.domain.Specification;
import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;

public final class SaidaSpecification {
	
	public static Specification<SaidaMaterial> buscarSaidaPeriodoMaterial( Date dataInicio, Date dataFim, Material material ){
		return new Specification<SaidaMaterial>() {
			public Predicate toPredicate(Root<SaidaMaterial> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.and();
				if(dataInicio !=null){
					predicate = builder.and(predicate, builder.lessThanOrEqualTo( root.get("data"), dataInicio) );
				}
				
				if(dataFim !=null){
					predicate = builder.and(predicate, builder.greaterThanOrEqualTo( root.get("data"), dataFim) );
				}
				
				if(material!=null){
					Subquery<SaidaMaterial> saidaQuery = query.subquery(SaidaMaterial.class);
					Root<ItemSaida> itemSaida =  saidaQuery.from(ItemSaida.class);
					Join<ItemSaida, SaidaMaterial> itensSaida = itemSaida.join("saidaMaterial");
					saidaQuery.select(itensSaida).where(builder.equal(itemSaida.get("material"),material));
					predicate = builder.and(predicate, builder.in(root).value(saidaQuery));
				}
				
				return predicate;
			}
		};
	}

}
