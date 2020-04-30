package ufc.npi.clinicas.teste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.clinicas.service.MaterialService;

@RestController
@RequestMapping("teste/relatorio")
public class RelatorioControllerTeste {
	
	@Autowired
	private MaterialService materialService;
	
	@RequestMapping(value = "/{setorId}/estoqueEntradaSaidaSetor", method = RequestMethod.GET)
	public ModelAndView estoqueEntradaSaidaSetor(Model model) {
		
		ModelAndView mav = new ModelAndView("relatorio/estoque_entrada_saida_setor")
			.addObject("entradas", new String[3])
			.addObject("materiais", materialService.listar());
		
		return mav;
		
	}
	
	@RequestMapping(value = "/materiaisValidade", method = RequestMethod.GET)
	public ModelAndView materiaisValidade(Model model) {
		
		ModelAndView mav = new ModelAndView("relatorio/materiais_validade")
			.addObject("materiais", materialService.listar());
		
		return mav;
		
	}
	
}
