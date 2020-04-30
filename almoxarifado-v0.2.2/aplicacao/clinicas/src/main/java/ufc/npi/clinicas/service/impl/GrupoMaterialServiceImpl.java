package ufc.npi.clinicas.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.GrupoMaterial;
import ufc.npi.clinicas.repository.GrupoMaterialRepository;
import ufc.npi.clinicas.service.GrupoMaterialService;
import ufc.npi.clinicas.util.Constants;

@Named
public class GrupoMaterialServiceImpl implements GrupoMaterialService{

	@Inject
	private GrupoMaterialRepository grupoMaterialRepository;

	public boolean temRepetido(GrupoMaterial grupo_material){
		List<GrupoMaterial> g = grupoMaterialRepository.findAll();
		int count = 0;
		for(GrupoMaterial grupo : g){
			if(grupo.getNome().equals(grupo_material.getNome())){
				count++;
			}
		}
		if(count>=1){
			return true;
		}
		return false;
	}
	
	
	@Override
	public void adicionar(GrupoMaterial grupoMaterial) throws ClinicasException {
		try{
			grupoMaterial.setCodigo(grupoMaterial.getCodigo().trim());
			grupoMaterial.setNome(grupoMaterial.getNome().trim());
			if (grupoMaterial.getCodigo() == null || grupoMaterial.getNome() == null || 
					grupoMaterial.getCodigo().isEmpty() || grupoMaterial.getNome().isEmpty()){
				throw new ClinicasException(Constants.ERRO_TODOS_CAMPOS_OBRIGATORIOS);
			}else if(temRepetido(grupoMaterial)){
				throw new ClinicasException(Constants.GRUPO_MATERIAL_ADICIONAR_ERRO_UNIQUE);
			}
			grupoMaterialRepository.save(grupoMaterial);			
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.GRUPO_MATERIAL_ADICIONAR_ERRO_UNIQUE);
		}
	}

	@Override
	public void editar(GrupoMaterial grupoMaterial) throws ClinicasException {
		try{
			grupoMaterial.setCodigo(grupoMaterial.getCodigo().trim());
			grupoMaterial.setNome(grupoMaterial.getNome().trim());
			if (grupoMaterial.getCodigo() == null || grupoMaterial.getNome() == null || 
					grupoMaterial.getCodigo().trim().isEmpty() || grupoMaterial.getCodigo().trim().isEmpty()){
				throw new ClinicasException(Constants.ERRO_TODOS_CAMPOS_OBRIGATORIOS);
			}else if(temRepetido(grupoMaterial)){
				throw new ClinicasException(Constants.GRUPO_MATERIAL_EDITAR_ERRO);
			}
			grupoMaterialRepository.save(grupoMaterial);			
		}catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.GRUPO_MATERIAL_EDITAR_ERRO);
		}
	}

	@Override
	public GrupoMaterial buscarPorId(Integer idGrupoMaterial) {
		return grupoMaterialRepository.findOne(idGrupoMaterial);
	}

	@Override
	public List<GrupoMaterial> listar() {
		return grupoMaterialRepository.findAllByOrderByNome();
	}

	@Override
	public void excluir(GrupoMaterial grupoMaterial) throws ClinicasException{
		try {
			grupoMaterialRepository.delete(grupoMaterial);
		} catch (DataIntegrityViolationException e) {
			throw new ClinicasException(Constants.GRUPO_MATERIAL_REMOVER_ERRO);
		}

	}

}
