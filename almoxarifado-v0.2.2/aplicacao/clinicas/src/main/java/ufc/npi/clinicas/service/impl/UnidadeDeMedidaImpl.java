package ufc.npi.clinicas.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.UnidadeMedida;
import ufc.npi.clinicas.repository.UnidadeMedidaRepository;
import ufc.npi.clinicas.service.UnidadeDeMedidaService;
import ufc.npi.clinicas.util.Constants;

@Named
public class UnidadeDeMedidaImpl implements UnidadeDeMedidaService{
	@Inject
	private UnidadeMedidaRepository unidadeMedidaRepository;

	@Override
	public void adicionar(UnidadeMedida unidadeMedida) throws ClinicasException {
		try {
			unidadeMedida.setNome(unidadeMedida.getNome().trim());
			if (unidadeMedida.getNome() == null || unidadeMedida.getNome().isEmpty()){
				throw new ClinicasException(Constants.ERRO_TODOS_CAMPOS_OBRIGATORIOS);
			}
			unidadeMedidaRepository.save(unidadeMedida);
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.UNIDADE_MEDIDA_ADICIONAR_ERRO);
		}
	}

	@Override
	public void editar(UnidadeMedida unidadeMedida) throws ClinicasException{
		try {
			unidadeMedidaRepository.save(unidadeMedida);
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.UNIDADE_MEDIDA_EDITAR_ERRO);
		}
	}

	@Override
	public UnidadeMedida buscarPorId(Integer idUnidadeMedida) {
		return unidadeMedidaRepository.findOne(idUnidadeMedida);
	}

	@Override
	public List<UnidadeMedida> listar() {
		return unidadeMedidaRepository.findAllByOrderByNomeAsc();
	}

	@Override
	public boolean excluir(UnidadeMedida unidadeMedida) {

		if(unidadeMedida!=null){
			try {
				unidadeMedidaRepository.delete(unidadeMedida);
			} catch (DataIntegrityViolationException e) {
				return false;
			}
		}else{
			return false;
		} 

		return true;

	}


}
