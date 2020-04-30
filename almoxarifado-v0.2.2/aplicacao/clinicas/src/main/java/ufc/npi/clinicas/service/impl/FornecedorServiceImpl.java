package ufc.npi.clinicas.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Fornecedor;
import ufc.npi.clinicas.repository.FornecedorRepository;
import ufc.npi.clinicas.service.FornecedorService;
import ufc.npi.clinicas.util.Constants;

@Named
public class FornecedorServiceImpl implements FornecedorService{
	
	@Inject
	private FornecedorRepository fornecedorRepository;
	
	public boolean temRepetido(Fornecedor fornecedor){
		List<Fornecedor> f = fornecedorRepository.findAll();
		int count = 0;
		for(Fornecedor fornec : f){
			if(fornec.getRazaoSocial().equals(fornecedor.getRazaoSocial())){
				count++;
			}
		}
		if(count>=1){
			return true;
		}
		return false;
	}
	
	@Override
	public void adicionar(Fornecedor fornecedor) throws ClinicasException{
		try{
			fornecedor.setCnpj(fornecedor.getCnpj().trim());
			fornecedor.setRazaoSocial(fornecedor.getRazaoSocial().trim());
			if (fornecedor.getCnpj() == null || fornecedor.getRazaoSocial() == null || 
					fornecedor.getCnpj().isEmpty() || fornecedor.getRazaoSocial().isEmpty()){
				throw new ClinicasException(Constants.FORNECEDOR_ADICIONAR_ERRO);
			}else if(temRepetido(fornecedor)){
				throw new ClinicasException(Constants.FORNECEDOR_ADICIONAR_ERRO_UNICO);
			}
			fornecedorRepository.save(fornecedor);	
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.FORNECEDOR_ADICIONAR_ERRO_UNICO);
		}
	}

	@Override
	public void editar(Fornecedor fornecedor) throws ClinicasException{
		try{
			fornecedor.setCnpj(fornecedor.getCnpj().trim());
			fornecedor.setRazaoSocial(fornecedor.getRazaoSocial().trim());
			if (fornecedor.getCnpj() == null || fornecedor.getRazaoSocial() == null || 
					fornecedor.getCnpj().isEmpty() || fornecedor.getRazaoSocial().isEmpty()){
				throw new ClinicasException(Constants.FORNECEDOR_EDITAR_ERRO);
			}else if(temRepetido(fornecedor)){
				throw new ClinicasException(Constants.FORNECEDOR_ADICIONAR_ERRO_UNICO);
			}
			fornecedorRepository.save(fornecedor);	
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.FORNECEDOR_ADICIONAR_ERRO_UNICO);
		}
	}

	@Override
	public Fornecedor buscarPorId(Integer idFornecedor) {
		return this.fornecedorRepository.findOne(idFornecedor);
	}

	@Override
	public List<Fornecedor> listar() {
		return this.fornecedorRepository.findAll();
	}

	@Override
	public void excluir(Fornecedor fornecedor) throws ClinicasException{
		try{
			this.fornecedorRepository.delete(fornecedor);		
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.FORNECEDOR_REMOVER_ERRO);
		}
	}
	
}
