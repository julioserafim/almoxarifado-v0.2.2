package ufc.npi.clinicas.controller;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.clinicas.service.MaterialService;

@RestController
@RequestMapping("configuracoes")
public class ConfiguracoesController {
	
	@Inject
	private MaterialService materialService;
	
	@GetMapping()
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("configuracoes/index");
		return mav;
	}
}
