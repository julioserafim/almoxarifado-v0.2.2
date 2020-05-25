package ufc.npi.clinicas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.npi.clinicas.exceptions.ClinicasException;
import ufc.npi.clinicas.model.SaidaMaterial;
import ufc.npi.clinicas.model.Status;
import ufc.npi.clinicas.service.EstoqueSetorService;
import ufc.npi.clinicas.service.SaidaMaterialService;
import ufc.npi.clinicas.service.SaidaService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.alert.AlertSet;

import javax.inject.Inject;

@Service
public class SaidaServiceImpl implements SaidaService {

    @Inject
    private SaidaMaterialService saidaMaterialService;

    @Inject
    private EstoqueSetorService estoqueSetorService;

    @Override
    public ModelAndView finalizarSaida(ModelAndView mav, RedirectAttributes redirectAttributes, Integer idSaida) {
        SaidaMaterial saida = saidaMaterialService.buscarPorId(idSaida);

        if(saida == null){
            mav.setViewName("redirect:/saida/listar");
            mav.addObject("erro", Constants.SAIDA_NAO_ENCONTRADA);
        }else if (saida.getStatus().equals(Status.FINALIZADA)){
            mav.setViewName(String.format("redirect:/saida/%1d/visualizar",saida.getId()));
            mav.addObject("sucesso", Constants.SAIDA_FINALIZAR_SUCESSO);
        }else{
            this.finalizarSaidaEmAndamento(saida, mav, redirectAttributes);
        }

        return mav;
    }

    private void finalizarSaidaEmAndamento(SaidaMaterial saida, ModelAndView mav, RedirectAttributes redirectAttributes) {
        try {
            if(saida.getItens().isEmpty()){
                mav.setViewName(String.format("redirect:/saida/%1d/incluirItens",saida.getId()));
                this.addWrongErrorAlert(redirectAttributes, Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE);
            }
            else{
                estoqueSetorService.atualizarEstoquesSetor(saida);
                saida.setStatus(Status.FINALIZADA);
                saidaMaterialService.editar(saida);

                mav.setViewName(String.format("redirect:/saida/%1d/visualizar",saida.getId()));
                redirectAttributes.addFlashAttribute("alertas", new AlertSet().withSuccess(Constants.SAIDA_FINALIZAR_SUCESSO));
            }
        } catch (ClinicasException e) {
            mav.setViewName(String.format("redirect:/saida/%1d/incluirItens",saida.getId()));
            this.addWrongErrorAlert(redirectAttributes, e.getMessage());
        }
    }

    private void addWrongErrorAlert(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("alertas", new AlertSet().withLongError(message));
    }

}
