package ufc.npi.clinicas.teste.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.clinicas.model.Entrada;
import ufc.npi.clinicas.model.SaidaMaterial;

@RestController
@RequestMapping("teste/saida")
public class SaidaControllerTeste {
	
	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public ModelAndView incluirItensEntrada(@Valid Entrada entrada, BindingResult result, Model model, Authentication auth){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("saida/visualizar_saida");
		return mav;
	}
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public ModelAndView listar(){
		ModelAndView mav = new ModelAndView();
		List<SaidaMaterial> saidas = new ArrayList<>();
		saidas.add(new SaidaMaterial());
		
		mav.addObject("saidas", saidas);
		mav.setViewName("saida/listar_saidas");
		return mav;
	}
	
	@RequestMapping(value = "/adicionar", method = RequestMethod.GET)
	public ModelAndView adicionar(@Valid Entrada entrada, BindingResult result, Model model, Authentication auth){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("saida/formulario_saida");
		return mav;
	}
	
	@RequestMapping(value = "/incluirItens", method = RequestMethod.GET)
	public ModelAndView incluirItensSaida(@Valid Entrada entrada, BindingResult result, Model model, Authentication auth){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("saida/formulario_incluir_itens_saida");
		return mav;
	}
	
}
