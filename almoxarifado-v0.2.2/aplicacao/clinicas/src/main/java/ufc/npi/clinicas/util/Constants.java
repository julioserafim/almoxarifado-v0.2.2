package ufc.npi.clinicas.util;

public class Constants {
	
	public static final String LOGIN_INVALIDO = "Usuário não encontrado e/ou senha inválidos";
	
	public static final String ENTRADA_ADICIONAR_ERRO = "Erro ao adicionar entrada";
	public static final String ENTRADA_ADICIONAR_ERRO_DATA_CHEGADA = "A data de chegada não deve ser posterior a data de hoje";
	public static final String ENTRADA_ADICIONAR_ERRO_VALOR_NOTA_FISCAL = "O valor da Nota Fiscal não deve ser negativo";
	public static final String ENTRADA_ADICIONAR_SUCESSO = "Entrada adicionada";
	public static final String ENTRADA_NAO_ENCONTRADA = "Entrada de Materiais não foi encontrada! Escolha uma nova entrada";
	public static final String ENTRADA_NOTA_EM_ANDAMENTO = "Nota ainda está em andamento! Por favor, inclua todos os materiais na nota e clique em salvar para prosseguir";
	public static final String ENTRADA_EXCLUIDA_ERRO = "Entrada não pode ser excluida, verifique os itens";
	public static final String ENTRADA_EXCLUIDA_SUCESSO = "Entrada excluida com sucesso";
	
	public static final String ENTRADA_INCLUIR_MATERIAIS_SOMA_ITENS_MAIOR = "A soma dos itens excede o valor total da nota";
	public static final String ENTRADA_INCLUIR_MATERIAIS_SOMA_ITENS_MENOR = "A soma dos itens é inferior ao valor total da nota";
	public static final String ENTRADA_INCLUIR_MATERIAIS_EXCLUIR_ITEM_ERRO = "Erro ao excluir item";
	public static final String ENTRADA_INCLUIR_MATERIAIS_EXCLUIR_ITEM_ERRO_NOTA_FINALIZADA = "Erro ao excluir item. Esta nota já está finalizada";
	public static final String ENTRADA_INCLUIR_MATERIAIS_FINALIZADA = "Inclusão de itens finalizada";
	public static final String ENTRADA_INCLUIR_MATERIAIS_FINALIZADA_ITEM_LIMITE = "Limite de itens atingido para esta nota";
	public static final String ENTRADA_INCLUIR_MATERIAIS_LOTE_DIFERENTE_VALIDADE = "Materiais com o mesmo lote devem ter a mesma data de validade";
	
	public static final String ENTRADA_ALOCAR_MATERIAIS_ERRO_SETOR = "o setor deve ser informado";
	public static final String ENTRADA_ALOCAR_MATERIAIS_ERRO_QUANTIDADE_NULA = "A quantidade de itens deve ser informada";
	public static final String ENTRADA_ALOCAR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ZERO = "A quantidade informada deve ser maior que zero";
	public static final String ENTRADA_ALOCAR_MATERIAIS_ERRO_QUANTIDADE_INDISPONIVEL = "A quantidade de itens disponíveis é menor que a quantidade informada";
	public static final String ENTRADA_ALOCAR_MATERIAIS_ERRO_TODOS_ITENS_ALOCADOS = "Todos os itens deste material já foram alocados";	
	public static final String ENTRADA_ALOCAR_MATERIAS_SUCESSO_ADICIONADO = "Alocação realizada com sucesso";
	public static final String ENTRADA_ALOCAR_MATERIAS_SUCESSO_VERIFICAR_FINALIZAÇAO = "Todos os materiais foram alocados. Finalizar Alocação disponível";
	public static final String ENTRADA_ALOCAR_COM_INCONSISTENCIA = "Ocorreu uma inconsistência na alocação dos materiais! Por favor, verifique se todos os materiais foram alocados para as disciplinas";
	public static final String ENTRADA_ALOCAR_FINALIZADA = "A alocação dos materiais dessa entrada para os setores já foi finalizada";
	
	public static final String ENTRADA_EDITAR_SUCESSO = "Entrada editada";
	public static final String ENTRADA_EDITAR_ERRO = "Erro ao editar a entrada";
	
	public static final String MATERIAL_ADICIONAR_SUCESSO = "Material adicionado";
	public static final String MATERIAL_ADICIONAR_ERRO = "Erro ao adicionar material";
	public static final String MATERIAL_EDITAR_SUCESSO = "Material editado";
	public static final String MATERIAL_EDITAR_ERRO = "Erro ao editar material";
	public static final String MATERIAL_EDITAR_CAMPOS = "Nome ou Código Interno não preenchidos";
	public static final String MATERIAL_EDITAR_ERRO_JA_EXISTE = "Material ou Código Interno já cadastrados";
	public static final String MATERIAL_REMOVER_SUCESSO = "Material removido";
	public static final String MATERIAL_REMOVER_ERRO = "Erro ao remover material";
	public static final String MATERIAL_CODIGO_BARRAS_EXISTE = "Código de barras já existe";
	public static final String MATERIAL_CODIGO_INTERNO_EXISTE = "Código inteno já existe";
	public static final String MATERIAL_CODIGO_BARRAS_ADCIONADO = "Código de barras adicionado";
	public static final String MATERIAL_ERRO_NAO_EXISTE = "Não existe este material cadastrado";
	public static final String MATERIAL_CODIGO_BARRAS_ADICIONAR_ERRO = "Erro ao adicionar o código de barras";
	public static final String MATERIAL_EXISTE_OU_CODIGO_INTERNO_EXISTE = "Material já existe ou código interno já existe";
	public static final String MATERIAL_UNIDADE_MEDIDA_VAZIA = "Erro ao adicionar Material, preencha unidade de medida";
	
	public static final String USUARIO_ADICIONAR_SUCESSO = "Usuário adicionado";
	public static final String USUARIO_ADICIONAR_ERRO = "Erro ao adicionar usuário";
	public static final String USUARIO_EXISTENTE_ERRO = "Erro ao adicionar usuário! Usuário existente";
	public static final String USUARIO_EDITAR_SUCESSO = "Usuário editado";
	public static final String USUARIO_EDITAR_ERRO = "Erro ao editar usuário";
	public static final String USUARIO_REMOVER_SUCESSO = "Usuário removido";
	public static final String USUARIO_REMOVER_ERRO = "Erro ao remover usuário";
	public static final String USUARIO_HABILITAR_ERRO = "Não foi possível mudar habilitação do usuário";
	
	public static final String UNIDADE_MEDIDA_ADICIONAR_SUCESSO = "Unidade de medida adicionada";
	public static final String UNIDADE_MEDIDA_ADICIONAR_ERRO = "Erro, unidade de medida já cadastrada";
	public static final String UNIDADE_MEDIDA_EDITAR_SUCESSO = "Unidade de medida editada";
	public static final String UNIDADE_MEDIDA_EDITAR_ERRO = "Erro ao editar unidade de medida";
	public static final String UNIDADE_MEDIDA_REMOVER_SUCESSO = "Unidade de medida removida";
	public static final String UNIDADE_MEDIDA_REMOVER_ERRO = "Erro ao remover unidade de medida";

	public static final String FORNECEDOR_ADICIONAR_SUCESSO = "Fornecedor adicionado";
	public static final String FORNECEDOR_ADICIONAR_ERRO = "Erro ao adicionar fornecedor, todos os campos devem ser preenchidos";
	public static final String FORNECEDOR_ADICIONAR_ERRO_UNICO = "Erro! CNPJ ou Razão Social já cadastrados";
	public static final String FORNECEDOR_EDITAR_SUCESSO = "Fornecedor editado";
	public static final String FORNECEDOR_EDITAR_ERRO = "Erro ao editar fornecedor";
	public static final String FORNECEDOR_REMOVER_SUCESSO = "Fornecedor removido";
	public static final String FORNECEDOR_REMOVER_ERRO = "Erro ao remover fornecedor";
	
	public static final String GRUPO_MATERIAL_ADICIONAR_SUCESSO = "Grupo de material adicionado";
	public static final String GRUPO_MATERIAL_ADICIONAR_ERRO_UNIQUE = "Os campos devem ser únicos. Nome ou código já cadastrados";
	public static final String GRUPO_MATERIAL_EDITAR_SUCESSO = "Grupo de material editado";
	public static final String GRUPO_MATERIAL_EDITAR_ERRO = "Erro ao editar grupo de material. Nome ou código já cadastrados";
	public static final String GRUPO_MATERIAL_REMOVER_SUCESSO = "Grupo de material removido";
	public static final String GRUPO_MATERIAL_REMOVER_ERRO = "Erro ao remover grupo de material";
	
    public static final String SETOR_ADICIONAR_SUCESSO = "Setor adicionado";
    public static final String SETOR_ADICIONAR_ERRO = "Erro ao adicionar setor. Código ou nome existente(s)";
    public static final String SETOR_EDITAR_SUCESSO = "Setor editado";
    public static final String SETOR_EDITAR_ERRO = "Erro ao editar setor";
    public static final String SETOR_EDITAR_ERRO_GERAL = "Erro! Não possível editar o setor geral";
    public static final String SETOR_REMOVER_SUCESSO = "Setor removido";
    public static final String SETOR_REMOVER_ERRO = "Erro ao remover setor";
    public static final String SETOR_REMOVER_ERRO_GERAL = "Erro! Não possível remover o setor geral";
    public static final String SETOR_BUSCAR_ESTOQUE_GERAL_ERRO = "Não há estoque geral cadastrado";
    
    public static final String ESTOQUE_SETOR_BAIXA_MATERIAL_ERRO = "Não foi possível registrar baixa do material %1s no lote %2s";
    public static final String ESTOQUE_SETOR_ESTOQUE_MATERIAL_INSUFICIENTE = "Estoque do material %1s é insuficiente";
    public static final String ESTOQUE_SETOR_ESTOQUE_MATERIAL_NO_SETOR_DO_LOTE_INSUFICIENTE = "Estoque do material %1s no lote %2s do setor %3s é insuficiente";
    
    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_MATERIAL_INEXISTENTE = "Não existe material com o código de barras informado";
    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_NAO_EXISTE_ESTOQUE_MATERIAL_SETOR = "Não há itens desse material no estoque do setor fonte da saída";
    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ESTOQUE = "A quantidade informada é maior que a quantidade em estoque";
    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE = "A quantidade informada está vazia, verifique os itens";
    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_MAIOR_ESTOQUE_LOTE = "A quantidade informada é maior que a quantidade em estoque para esse lote";
    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_ITEM_SAIDA_EXISTENTE = "Esse material já foi adicionado a saída. Para alterar a quantidade, remova-o e adicione novamente";
    public static final String SAIDA_INCLUIR_MATERIAIS_SUCESSO_ADICIONADO = "Inclusão de material realizada com sucesso";

    public static final String SAIDA_INCLUIR_MATERIAIS_SUCESSO_REMOVER = "Remoção de material realizada com sucesso";
    public static final String SAIDA_INCLUIR_MATERIAIS_REMOVER_ERRO = "Erro ao remover material";
    public static final String SAIDA_INCLUIR_MATERIAIS_REMOVER_NULL = "Item não existe ou já foi excluído";

    public static final String SAIDA_INCLUIR_MATERIAIS_ERRO_QUANTIDADE_INVALIDA = "A quantidade deve ser maior que zero";
    
    public static final String SAIDA_EDITAR_SUCESSO = "Saida editada";
    public static final String SAIDA_EDITAR_ERRO = "Erro ao editar a saida";
    
	public static final String REDIRECT_INCLUIR_MATERIAIS_ENTRADA = "redirect:/entrada/incluirMateriais/";
	public static final String REDIRECT_VISUALIZAR_ENTRADA = "redirect:/entrada/visualizar/";
	public static final String REDIRECT_VISUALIZAR_MATERIAL = "redirect:/material/id/visualizar";
	
	public static final String SAIDA_NAO_ENCONTRADA = "Saída não encontrada";
	public static final String SAIDA_EXCLUIDA_ERRO = "Saída não pode ser excluida, verifique os itens";
	public static final String SAIDA_EXCLUIDA_SUCESSO = "Saída excluida com sucesso";
	public static final String SAIDA_FINALIZAR_SUCESSO = "Saída de materias registrada com sucesso";
	public static final String SAIDA_FINALIZAR_ERRO = "Erro ao finalizar saída de materiais";
	
	public static final String DATA_ANTERIOR_ATUAL = "Data anterior ao dia atual";
	
	public static final String ERRO_TODOS_CAMPOS_OBRIGATORIOS = "Todos os campos devem ser preenchidos";
	
	public static final String DATA_INICIO_MAIOR_FIM = "A data de início não pode ser maior que a data final";
}
