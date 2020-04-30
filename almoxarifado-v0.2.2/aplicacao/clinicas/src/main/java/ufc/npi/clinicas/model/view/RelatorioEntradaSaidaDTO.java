package ufc.npi.clinicas.model.view;

import java.util.Date;

public class RelatorioEntradaSaidaDTO {

    public RelatorioEntradaSaidaDTO(String nome, String unidadeMedida, long quantidade, Date data) {
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
        this.quantidade = quantidade;
        this.data = data;
    }

    private String nome;

    private String unidadeMedida;

    private long quantidade;

    private Date data;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
