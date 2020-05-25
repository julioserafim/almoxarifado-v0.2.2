package ufc.npi.clinicas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.Setor;
import ufc.npi.clinicas.service.ItemSaidaService;
import ufc.npi.clinicas.service.RelatorioService;
import ufc.npi.clinicas.service.SetorService;
import ufc.npi.clinicas.util.alert.AlertSet;

import javax.inject.Inject;
import java.util.Date;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    @Inject
    private SetorService setorService;

    @Inject
    private ItemSaidaService itemSaidaService;

    @Override
    public ModelAndView gerarMediaHistorica(
            Date inicio,
            Date fim,
            Date anoInicio,
            Date anoFim,
            Setor setor,
            String tipoBusca,
            Integer semestreInicio,
            Integer semestreFim
    ) {

        boolean buscarPeriodo = tipoBusca.equals("busca_periodo");

        ModelAndView modelAndView = new ModelAndView("relatorio/media_historica_materiais")
                .addObject("setores", setorService.listar())
                .addObject("dataInicio", inicio)
                .addObject("dataFim", fim)
                .addObject("anoInicio", anoInicio)
                .addObject("anoFim", anoFim)
                .addObject("setorSelecionado", setor)
                .addObject("buscarPeriodo", buscarPeriodo)
                .addObject("busca", true);

        if (buscarPeriodo) {
            this.buscarSaidasPorPeriodo(modelAndView, setor, inicio, fim);
        } else {
            this.buscarSaidasPorSemestreAno(modelAndView, setor, semestreInicio, semestreFim, anoInicio, anoFim);
        }

        return modelAndView;
    }

    private void buscarSaidasPorPeriodo(ModelAndView modelAndView, Setor setor, Date inicio, Date fim) {
        try {
            modelAndView.addObject("saidas", itemSaidaService.getMediaSaidasPorPeriodo(setor, inicio, fim));
        } catch (ClinicasException e) {
            this.addAlert(modelAndView, e.getMessage());
        }
    }

    private void buscarSaidasPorSemestreAno(ModelAndView modelAndView, Setor setor, Integer semestreInicio, Integer semestreFim, Date anoInicio, Date anoFim) {
        try {
            modelAndView.addObject("saidas", itemSaidaService.getMediaSaidasPorSemestre(setor, semestreInicio, semestreFim, anoInicio, anoFim));
        } catch (ClinicasException e) {
            this.addAlert(modelAndView, e.getMessage());
        }
    }

    private void addAlert(ModelAndView modelAndView, String message) {
        modelAndView.addObject("alertas", new AlertSet().withLongWarning(message));
    }
}
