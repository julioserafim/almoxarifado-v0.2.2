package ufc.npi.clinicas.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.Status;
import ufc.npi.clinicas.model.TipoSaida;
import ufc.npi.clinicas.repository.SaidaMaterialRepository;
import ufc.npi.clinicas.repository.UsuarioRepository;
import ufc.npi.clinicas.service.SaidaMaterialService;
import ufc.npi.clinicas.specification.RelatorioSpecification;
import ufc.npi.clinicas.specification.SaidaSpecification;
import ufc.npi.clinicas.util.Constants;

@Named
public class SaidaMaterialServiceImpl implements SaidaMaterialService{
	
	@Inject
	private SaidaMaterialRepository saidaMaterialRepository;

	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Override
	public List<SaidaMaterial> listar() {
		return saidaMaterialRepository.findAllByOrderByDataDesc();
	}


	@Override
	public void adicionarSaida(SaidaMaterial saidaMaterial, String emailResponsavel) {
		saidaMaterial.setStatus(Status.EM_ANDAMENTO);
		saidaMaterial.setResponsavel(usuarioRepository.findByEmail(emailResponsavel));
		if (saidaMaterial.getTipo() == TipoSaida.SETOR && saidaMaterial.getDestino() == null){
			saidaMaterial.setDestino(saidaMaterial.getOrigem());
		}
		saidaMaterialRepository.save(saidaMaterial);
		
	}
	
	@Override
	public SaidaMaterial buscarPorId(Integer idSaida) {
		return saidaMaterialRepository.findOne(idSaida);

	}

	@Override
	public void editar(SaidaMaterial saida) throws ClinicasException {
		if (saida == null) {
			throw new ClinicasException(Constants.SAIDA_EDITAR_ERRO);
		}
		saidaMaterialRepository.save(saida);
	}
	

	@Override
	public List<SaidaMaterial> buscarPorDataEMaterial(Date dataInicio, Date dataFim, Material material) {
		Specification<SaidaMaterial> saidaSpecification = SaidaSpecification.buscarSaidaPeriodoMaterial(dataInicio,dataFim,material);
		return saidaMaterialRepository.findAll(saidaSpecification,new Sort(Direction.DESC,"data") );
		
	}
	@Override
	public List<SaidaMaterial> listarPorOrigemEData(Setor setor, Date dataInicio, Date dataFim) {
		Specification<SaidaMaterial> saidaSpecification = RelatorioSpecification.buscarSaidaPeriodoOrigem(dataInicio, dataFim, setor);
		return saidaMaterialRepository.findAll(saidaSpecification, new Sort(Direction.DESC,"data"));
	}


	@Override
	public List<SaidaMaterial> buscarPorOrigem(Integer idOrigem) {
		return saidaMaterialRepository.getByOrigem(idOrigem);
	}	

	@Override
	public void excluir(SaidaMaterial saida){
		saidaMaterialRepository.delete(saida);
	}


	@Override
	public Integer buscarTotalStatus(Status status) {
		return saidaMaterialRepository.getTotalStatus(status);
	}

}
