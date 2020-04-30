package ufc.npi.clinicas.service;

import java.util.Date;
import java.util.List;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;

public interface EntradaService {
	
	List<Entrada> listar();
	
	void adicionar(Entrada entrada, String emailResponsavel) throws ClinicasException;
	
	void editar(Entrada entrada) throws ClinicasException;
	
	Entrada buscarPorId(Integer idEntrada);
	
	ItemEntrada buscarItemPorId(Integer idItemEntrada);
	
	void removerItem(Entrada entrada, ItemEntrada item);

	void adicionarItemEntrada (ItemEntrada itemEntrada) throws ClinicasException;
	
	boolean finalizarAlocacaoEntrada(Entrada entrada) throws ClinicasException;


	List<Entrada> buscarPorItens(List<ItemEntrada> itens);
	
	List<Entrada> buscarPorDataEMaterial(Date dataInicio, Date dataFim, Material material);

	List<Entrada> buscarPorSetor(Integer idSetor);
	
	List<Entrada> buscarPorSetorEData(Setor setor, Date dataInicio, Date DataFim);

	void excluir(Entrada entrada);

}
