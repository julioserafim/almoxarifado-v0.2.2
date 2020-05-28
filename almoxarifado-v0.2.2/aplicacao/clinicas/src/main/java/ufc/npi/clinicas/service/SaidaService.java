package ufc.npi.clinicas.service;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface SaidaService {

    ModelAndView finalizarSaida(ModelAndView mav, RedirectAttributes redirectAttributes, Integer idSaida);
}
