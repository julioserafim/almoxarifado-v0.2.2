package ufc.npi.clinicas.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.AlocacaoItemSetor;
import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.EstoqueLote;
import ufc.npi.clinicas.model.EstoqueSetor;
import ufc.npi.clinicas.model.ItemEntrada;
import ufc.npi.clinicas.model.Material;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.model.StatusEntrada;
import ufc.npi.clinicas.model.Usuario;
import ufc.npi.clinicas.repository.EntradaRepository;
import ufc.npi.clinicas.repository.ItemEntradaRepository;
import ufc.npi.clinicas.repository.UsuarioRepository;
import ufc.npi.clinicas.service.AlocacaoItemSetorService;
import ufc.npi.clinicas.service.EntradaService;
import ufc.npi.clinicas.service.EstoqueLoteService;
import ufc.npi.clinicas.service.EstoqueSetorService;
import ufc.npi.clinicas.service.MaterialService;
import ufc.npi.clinicas.specification.EntradaSpecification;
import ufc.npi.clinicas.specification.RelatorioSpecification;
import ufc.npi.clinicas.util.Constants;

@Named
public class EntradaServiceImpl implements EntradaService{
	@Inject
	private EntradaRepository entradaRepository;
	@Inject
	private UsuarioRepository usuarioRepository;
	@Inject
	private ItemEntradaRepository itemRepository;
	@Inject
	private EstoqueSetorService estoqueSetorService;
	@Inject
	private AlocacaoItemSetorService alocacaoItemSetorService;
	@Inject
	private MaterialService materialService;
	@Inject 
	private EstoqueLoteService estoqueLoteService;
	
	@Override
	public void adicionar(Entrada entrada, String emailResponsavel) throws ClinicasException{
		boolean erroCamposObrigatorios = false;
		
		String msg = Constants.ERRO_TODOS_CAMPOS_OBRIGATORIOS;
		
		switch(entrada.getTipo()){
		case AJUSTE_ESTOQUE:
			erroCamposObrigatorios = (entrada.getChegada() == null);
			break;
		case DEVOLUCAO: 
			erroCamposObrigatorios = (entrada.getChegada() == null || entrada.getSetor() == null);
			break;
		case DOACAO:
			erroCamposObrigatorios = (entrada.getChegada() == null);
			msg = "Preeencha os campos obrigatórios";
			break;
		case FORNECEDOR:
			erroCamposObrigatorios = entrada.getChegada() == null || entrada.getFornecedor() == null || entrada.getValorNotaFiscal() == null;
			msg = "Preeencha os campos obrigatórios";
			break;
		case OUTROS:
			erroCamposObrigatorios = (entrada.getChegada() == null);
			break;
		default:
			break;
		}
		if (erroCamposObrigatorios){
			throw new ClinicasException(msg);
		}
		
		if (entrada.getChegada().after(new Date())){
			throw new ClinicasException(Constants.ENTRADA_ADICIONAR_ERRO_DATA_CHEGADA);
		}
		if (entrada.getValorNotaFiscal() != null && entrada.getValorNotaFiscal().compareTo(BigDecimal.ZERO) < 0){
			throw new ClinicasException(Constants.ENTRADA_ADICIONAR_ERRO_VALOR_NOTA_FISCAL);
		}
		
		Usuario responsavel =  usuarioRepository.findByEmail(emailResponsavel);
		entrada.setResponsavel(responsavel);
		entrada.setStatusEntrada(StatusEntrada.PENDENTE_INCLUSAO);
		
		
		entradaRepository.save(entrada);
	}
	
	@Override
	public void editar(Entrada entrada) throws ClinicasException{	
		if (entrada == null) {
			throw new ClinicasException(Constants.ENTRADA_EDITAR_ERRO); 
		}
		
		entradaRepository.save(entrada);
		
	}	

	@Override
	public Entrada buscarPorId(Integer idEntrada) {		
		return entradaRepository.findOne(idEntrada);
		
	}

	@Override
	public ItemEntrada buscarItemPorId(Integer idItemEntrada) {
		return itemRepository.findOne(idItemEntrada);
	}

	@Override
	public void removerItem(Entrada entrada, ItemEntrada item) {	
			entrada.removeItem(item);
			itemRepository.delete(item);				
	}
	
	public void adicionarItemEntrada(ItemEntrada itemEntrada) throws ClinicasException {
		//Busca se já existe um item entrada deste material com este lote, se já houver a data de validade deverá ser a mesma, pois é o mesmo produto com um valor/quantidade diferente
		List<ItemEntrada> anterior = itemRepository.findByLoteAndMaterial(itemEntrada.getLote(), itemEntrada.getMaterial());
		if (anterior != null && !anterior.isEmpty() && itemEntrada.getValidade() != null && itemEntrada.getValidade().compareTo(anterior.get(0).getValidade()) != 0){
			throw new ClinicasException(Constants.ENTRADA_INCLUIR_MATERIAIS_LOTE_DIFERENTE_VALIDADE);
		}
		itemRepository.save(itemEntrada);

	}

	@Override
	public List<Entrada> listar() {
		return entradaRepository.findAllByOrderByChegadaDesc();
	}

	@Override
	public boolean finalizarAlocacaoEntrada(Entrada entrada) throws ClinicasException{
		for (ItemEntrada itemEntrada : entrada.getItens()) { 
			List<AlocacaoItemSetor> alocacoesItemSetors = alocacaoItemSetorService.buscarPorItemEntrada(itemEntrada.getId());
			if(alocacoesItemSetors == null || !validarAlocacaoItemSetor(itemEntrada, alocacoesItemSetors)){
				return false; 
			}
			Material material = itemEntrada.getMaterial();
			for(AlocacaoItemSetor alocacaoItemSetor : alocacoesItemSetors){
				updateEstoqueSetor(itemEntrada, alocacaoItemSetor);
				material.setEstoque(material.getEstoque() + alocacaoItemSetor.getQuantidade());				
			}
			materialService.editar(material);
			
			// Atualização do estoqueLote
			EstoqueLote estoqueLote = updateEstoqueLote(itemEntrada, material);
			estoqueLoteService.salvar(estoqueLote);
			
		}
		return true;
	}

	private void updateEstoqueSetor(ItemEntrada itemEntrada, AlocacaoItemSetor alocacaoItemSetor) {
		EstoqueSetor estoqueSetor = this.estoqueSetorService.buscarPorSetorEMaterial(alocacaoItemSetor.getSetor(), itemEntrada.getMaterial());
		if(estoqueSetor == null){
			//caso não exista um estoqueSetor com os atributos passados por parametro, cria um novo.
			estoqueSetor = createEstoqueSetor(itemEntrada, alocacaoItemSetor);
		}
		//modifica a quantidade e depois salva.
		estoqueSetor.setQuantidade(estoqueSetor.getQuantidade() + alocacaoItemSetor.getQuantidade());
		estoqueSetorService.editar(estoqueSetor);
	}

	private EstoqueSetor createEstoqueSetor(ItemEntrada itemEntrada, AlocacaoItemSetor alocacaoItemSetor) {
		EstoqueSetor estoqueSetor;
		estoqueSetor = new EstoqueSetor();
		estoqueSetor.setSetor(alocacaoItemSetor.getSetor());
		estoqueSetor.setMaterial(itemEntrada.getMaterial());
		estoqueSetor.setQuantidade(0);
		estoqueSetorService.adicionar(estoqueSetor);
		return estoqueSetor;
	}

	private EstoqueLote updateEstoqueLote(ItemEntrada itemEntrada, Material material) {
		EstoqueLote estoqueLote = estoqueLoteService.buscarPorMaterialLote(material, itemEntrada.getLote());
		if(estoqueLote == null){
			estoqueLote = new EstoqueLote();
			estoqueLote.setLote(itemEntrada.getLote());
			estoqueLote.setMaterial(material);
			estoqueLote.setQuantidade(itemEntrada.getQuantidade());
			estoqueLote.setValidade(itemEntrada.getValidade());
		}else {
			estoqueLote.atualizaQuantidade(itemEntrada.getQuantidade());
		}
		return estoqueLote;
	}
	
	private boolean validarAlocacaoItemSetor(ItemEntrada itemEntrada, List<AlocacaoItemSetor> alocacoesItemSetors){
		int quantidade=0;
		for(AlocacaoItemSetor alocacaoItemSetor: alocacoesItemSetors){
			quantidade += alocacaoItemSetor.getQuantidade();
		}
		return itemEntrada.getQuantidade() == quantidade;
	}

	@Override
	public List<Entrada> buscarPorItens(List<ItemEntrada> itens) {
		return entradaRepository.findByItens(itens); 
	}

	@Override
	public List<Entrada> buscarPorDataEMaterial(Date dataInicio, Date dataFim, Material material) {
		Specification<Entrada> entradaSpecification = EntradaSpecification.buscarEntradaPeriodoMaterial(dataInicio,dataFim,material);
		return entradaRepository.findAll(entradaSpecification,new Sort(Direction.DESC,"chegada"));
	}
	
	@Override
	public List<Entrada> buscarPorSetorEData(Setor setor, Date dataInicio, Date dataFim) {
		Specification<Entrada> entradaSpecification = RelatorioSpecification.buscarEntradaPeriodoSetor(dataInicio, dataFim, setor);
		return entradaRepository.findAll(entradaSpecification, new Sort(Direction.DESC,"chegada"));

	}

	@Override
	public List<Entrada> buscarPorSetor(Integer idSetor) {
		return entradaRepository.getBySetor(idSetor);
	}

	@Override
	public void excluir(Entrada entrada) {
		entradaRepository.delete(entrada);		
	}

	
}
