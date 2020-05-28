package ufc.npi.clinicas.service;

import org.springframework.web.servlet.ModelAndView;
import ufc.npi.clinicas.model.Setor;

import java.util.Date;

public interface RelatorioService {

    ModelAndView gerarMediaHistorica(
            Date inicio,
            Date fim,
            Date anoInicio,
            Date anoFim,
            Setor setor,
            String tipoBusca,
            Integer semestreInicio,
            Integer semestreFim
    );

}

