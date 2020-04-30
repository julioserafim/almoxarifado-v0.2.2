package ufc.npi.clinicas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClinicasController {
	
	@GetMapping("/")
	public ModelAndView index() {

// EXEMPLOS DE MENSAGENS DE ALERTA		
//		AlertSet alertas = new AlertSet()
//				.withInfo("Exemplo de informação") // Mensagem com 3s de duração
//				.withLongWarning("Exemplo de aviso") // Mensagem de 5s de duração
//				.withSuccess("Exemplo de sucesso")
//				.withLongError("Exemplo de erro");
//		
		return new ModelAndView("index");
		
	}

}
