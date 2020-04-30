package ufc.npi.clinicas.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.repository.SetorRepository;
import ufc.npi.clinicas.service.SetorService;
import ufc.npi.clinicas.util.Constants;

@Named
public class SetorServiceImpl implements SetorService{
	@Inject
	private SetorRepository setorRepository;
	
	public boolean temRepetido(Setor setor){
		List<Setor> s = setorRepository.findAll();
		int count = 0;
		for(Setor setores : s){
			if(setores.getNome().equals(setor.getNome())){
				count++;
			}
		}
		if(count>=1){
			return true;
		}
		return false;
	}
	
	
	@Override
	public void adicionar(Setor setor) throws ClinicasException{
			
		try {
			setor.setNome(setor.getNome().trim());
			setor.setCodigo(setor.getCodigo().trim());
			if (setor.getNome() == null || setor.getCodigo() == null || 
					setor.getNome().isEmpty() || setor.getCodigo().isEmpty()){
				throw new ClinicasException(Constants.SETOR_ADICIONAR_ERRO);
			}else if(temRepetido(setor)){
				throw new ClinicasException(Constants.SETOR_ADICIONAR_ERRO);
			}
			setorRepository.save(setor);	
		} catch (DataIntegrityViolationException  e) {
			throw new ClinicasException(Constants.SETOR_ADICIONAR_ERRO);
		}
	}

	@Override
	public void editar(Setor setor)  throws ClinicasException{
		
		try {
			if (setor.isGeral()){
				throw new ClinicasException(Constants.SETOR_EDITAR_ERRO_GERAL);
			}
			setor.setNome(setor.getNome().trim());
			setor.setCodigo(setor.getCodigo().trim());
			if (setor.getNome() == null || setor.getCodigo() == null || 
					setor.getNome().isEmpty() || setor.getCodigo().isEmpty()){
				throw new ClinicasException(Constants.SETOR_EDITAR_ERRO);
			}else if(temRepetido(setor)){
				throw new ClinicasException(Constants.SETOR_EDITAR_ERRO);
			}
			setorRepository.save(setor);	
		} catch (DataIntegrityViolationException  e) {
			throw new ClinicasException(Constants.SETOR_EDITAR_ERRO);
		}
	}

	@Override
	public Setor buscarPorId(Integer idsetor) {
		return setorRepository.findOne(idsetor);
	}

	@Override
	public List<Setor> listar() { 
		return setorRepository.findAll();
	}

	@Override
	public void excluir(Setor setor) throws ClinicasException{
		if (setor.isGeral()){
			throw new ClinicasException(Constants.SETOR_REMOVER_ERRO_GERAL);
		}
		else if (setor!=null){
			try {
				setorRepository.delete(setor);
			} catch (DataIntegrityViolationException  e) {
				throw new ClinicasException(Constants.SETOR_REMOVER_ERRO);
			}
		}
	}

	@Override
	public List<Setor> setoresDestino() {
		return setorRepository.setoresDestino();
	}

	@Override
	public Setor estoqueGeral() {
		try {
			Setor setor = setorRepository.estoqueGeral().get(0);
			return setor;
			
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		
		
	}

}
