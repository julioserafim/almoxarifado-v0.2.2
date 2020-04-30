package ufc.npi.clinicas.teste.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.Fornecedor;

import ufc.npi.clinicas.service.MaterialService;
import ufc.npi.clinicas.service.SetorService;

@RestController
@RequestMapping("teste/entrada")
public class EntradaControllerTeste {
	
	@Inject
	private MaterialService materialService;
	@Inject
	private SetorService setorService;
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public ModelAndView listar() {
		
		// Criando uma entrada para teste
		Entrada entrada = new Entrada();
		entrada.setChegada(new Date());
		entrada.setEmissaoNotaFiscal(new Date());
		entrada.setEmpenho("Empenhadíssimo");
		entrada.setNotaFiscal("Nota Fiscal Tal");
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setRazaoSocial("Rico");
		entrada.setFornecedor(fornecedor);
		List<Entrada> entradas = new ArrayList<>();
		entradas.add(entrada);
		entradas.add(entrada);
		entradas.add(entrada);
		entradas.add(entrada);
		entradas.add(entrada);
		ModelAndView mav = new ModelAndView();
		mav.addObject("entradas", entradas);
		mav.setViewName("entrada/listar_entradas");
		return mav;
	}
		
	@RequestMapping(value = "/alocarItens", method = RequestMethod.GET)
	public ModelAndView alocarItensDisciplina(@Valid Entrada entrada, BindingResult result, Model model, Authentication auth) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("entrada/formulario_alocar_itens_disciplinas");
		mav.addObject("materiais", materialService.listar());
		mav.addObject("setores", setorService.listar());
		return mav;
	}
}
