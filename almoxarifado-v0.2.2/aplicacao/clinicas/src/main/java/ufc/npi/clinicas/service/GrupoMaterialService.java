package ufc.npi.clinicas.service;

import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.GrupoMaterial;


public interface GrupoMaterialService {


	void adicionar(GrupoMaterial grupoMaterial) throws ClinicasException;

	void editar(GrupoMaterial grupoMaterial) throws ClinicasException;

	GrupoMaterial buscarPorId(Integer idGrupoMaterial);

	List<GrupoMaterial> listar();

	void excluir(GrupoMaterial grupoMaterial) throws ClinicasException;

}
