package ufc.npi.clinicas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufc.npi.clinicas.model.*;
import ufc.npi.clinicas.service.*;
import ufc.npi.clinicas.util.Constants;
import ufc.npi.clinicas.util.api.Response;

import javax.inject.Inject;

@Controller
@RequestMapping("item-saida")
public class ItemSaidaController {

    @Inject
    private EstoqueLoteService estoqueLoteService;

    @Inject
    private SaidaMaterialService saidaMaterialService;

    @Inject
    private SetorService setorService;

    @Inject
    private EstoqueSetorService estoqueSetorService;

    @Inject
    private ItemSaidaService itemSaidaService;

    @PostMapping("/api/itemSaida/adicionar")
    public Response adicionarItemSaida(Integer quantidade, Integer idSaida, Integer idMaterial, Integer idEstoqueLote, Integer idSetor){

        EstoqueLote estoqueLote = estoqueLoteService.buscarPorId(idEstoqueLote);

        Material material = estoqueLote.getMaterial();

        SaidaMaterial saidaMaterial = this.saidaMaterialService.buscarPorId(idSaida);

        ItemSaida itemSaida = new ItemSaida();
        itemSaida.setSaidaMaterial(saidaMaterial);
        itemSaida.setMaterial(material);
        itemSaida.setQuantidade(quantidade);
        itemSaida.setLote(estoqueLote.getLote());

        Setor setor = setorService.buscarPorId(idSetor);
        EstoqueSetor estoqueSetor = estoqueSetorService.buscarPorSetorEMaterial(setor, material);


        if(this.itemSaidaService.existePorSaidaMaterialEMaterialELote(saidaMaterial, material, estoqueLote.getLote())){

            return new Response().
                    withFailStatus().
                    withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_ITEM_SAIDA_EXISTENTE);

        }else{
            if (estoqueSetor == null || itemSaida.getQuantidade() > estoqueSetor.getQuantidade()){
                return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ESTOQUE);
            }else if(itemSaida.getQuantidade() > estoqueLote.getQuantidade()){
                return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ESTOQUE_LOTE);
            }else if(itemSaida.getQuantidade() <= 0){
                return new Response().withFailStatus().withErrorMessage(Constants.SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_INVALIDA);

            }else{
                itemSaida.setSaidaMaterial(saidaMaterial);
                itemSaida.setMaterial(material);
                return new Response().withDoneStatus().withObject(this.itemSaidaService.adicionar(itemSaida))
                        .withSuccessMessage(Constants.SAIDA_INCLUIR_MATERIAIS_SUCESSO_ADICIONADO);
            }
        }
    }

}
