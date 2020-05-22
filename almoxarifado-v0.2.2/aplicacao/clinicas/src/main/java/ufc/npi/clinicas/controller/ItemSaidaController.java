package ufc.npi.clinicas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ufc.npi.clinicas.model.ItemSaida;
import ufc.npi.clinicas.model.Status;
import ufc.npi.clinicas.service.ItemSaidaService;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.api.Response;

import javax.inject.Inject;

@Controller
@RequestMapping("item-saida")
public class ItemSaidaController {

    @Inject
    private ItemSaidaService itemSaidaService;

    @GetMapping(value = "saidaMaterial/remover/{idItemSaidaMaterial}")
    public Response excluiAlocacaoItemMaterial(@PathVariable("idItemSaidaMaterial") Long idItemSaidaMaterial) {
        ItemSaida itemSaida = itemSaidaService.buscarPorId(idItemSaidaMaterial);

        if (itemSaida != null) {
            if (itemSaida.getSaidaMaterial().getStatus().equals(Status.EM_ANDAMENTO)) {
                itemSaidaService.excluirItemSaidaMaterial(idItemSaidaMaterial);
                return new Response().withSuccessMessage(Constants.SAIDA_INCLUIR_MATERIAIS_SUCESSO_REMOVER);
            }
        }
        else{
            return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_REMOVER_NULL);
        }
        return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_REMOVER_ERRO);
    }

}
