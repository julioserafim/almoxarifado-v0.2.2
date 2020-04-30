//Verifica se campos de saida de material estão vazios
// não estão aplicadas ainda na saida de materiais
function verifica(){
	var v = document.getElementById("quantidade");
	if(v.value == ''){
		mostrarMensagem('Por favor, preencha os campos obrigatórios.', "error");
		return false;
	}
	v = document.getElementById("cbarras");
	if(v.value==''){
		mostrarMensagem('Por favor, preencha os campos obrigatórios.', "error");
		return false;
	}
	return true;
	
}


$(document).ready(function() {
	bloquearEnter();
	// Formulário da entrada
	$('select').material_select();
	$('.datepicker').pickadate({
		selectMonths: true,
		selectYears: 15,
		format: 'dd/mm/yyyy'
	});	            
	//////////PARA AJUDAR A FAZER O CONTROLE DO SELECT 
	$("#setor-origem").change( function(){
		if ($(this).val() == 1){
			$("#setor-destino").attr("disabled", "disabled");
			$("#setor-destino").material_select();
		}
		else{
			$("#setor-destino").attr("disabled", "disabled");
			$("#setor-destino").material_select();			
		}
	});
	//////////
	
//	$("#cbarras").click(function(){
//		$(this).val("");
//	});

	var materiais = [];
	var autocomplete = $(".cbarras-autocomplete").materialize_autocomplete({
		multiple: {
			enable: false
		},
		cacheable: false,
		getData: function (value, callback) {
			if(!isNaN(parseFloat(value))){
				var url = _context + "/saida/api/buscarMaterialEstoqueSetor/cbarras/" + value;
				var setor = {
						id: $("#setor-origem-id").val()
				};
				var token = $("meta[name='_csrf']").attr("content");
				$.ajax({
					dataType: "json",
					url: url,
					type: 'post',
					headers: {"X-CSRF-TOKEN":token},
					data: setor,
					success: function(responseMateriais) {
						materiais = responseMateriais.object;

						if(responseMateriais.object != null){
							var data = responseMateriais.object.map(function(m) {
								
								var materiais = m.material.codigos;
								
								var codigo;
								
								return { id: m.id, text: m.material.nome + " | " + m.material.unidadeMedida.nome+ " | Lote: " + m.lote + " | " + moment(new Date(m.validade)).format("DD/MM/YYYY") };
							});
							callback(value, data);
						}else{
							var data = [];
							callback(value, data);
						}

					}
				});
			}
			if(value.length>3){
				var url = _context + "/saida/api/buscarMaterialEstoqueSetor/cbarras/" + value;
				var setor = {
						id: $("#setor-origem-id").val()
				};
				var token = $("meta[name='_csrf']").attr("content");
				$.ajax({
					dataType: "json",
					url: url,
					type: 'post',
					headers: {"X-CSRF-TOKEN":token},
					data: setor,
					success: function(responseMateriais) {
						materiais = responseMateriais.object;
						if(responseMateriais.object != null){
							var data = responseMateriais.object.map(function(m) {
								
								var materiais = m.material.codigos;
								
								var codigo;
								
								return { id: m.id, text: m.material.nome + " | " + m.material.unidadeMedida.nome + " | Lote: " + m.lote + " | " + moment(new Date(m.validade)).format("DD/MM/YYYY")};
							});
							callback(value, data);
						}else{
							var data = [];
							callback(value, data);
						}
	
					}
				});
			}
		}, 
		onSelected: function(item) {
			var id = item.id;
			if(materiais != null){
				var estoqueLote = materiais.filter(function(m) { return m.id === id; })[0];
			}
			
			if(estoqueLote) {
				$("#estoque-lote-id").val(estoqueLote.id);
				$(".nome-material").text(estoqueLote.material.nome);
				$(".lote-material").text(estoqueLote.lote);
				$(".validade-material").text(moment(new Date(estoqueLote.validade)).format("DD/MM/YYYY"));
				$(".codigo-interno-material").text(estoqueLote.material.codigoInterno);
				$(".estoque-material").text(estoqueLote.quantidade);
				$(".rounded-info").removeClass("rounded-info-hide");
//				$(".dynamic-list").removeClass("dynamic-list-hide");
				$(".dynamic-list-add").removeAttr("disabled");


				$("#material-id").val(item.id);
			} else {
				$(".rounded-info").addClass("rounded-info-hide");
//				$(".dynamic-list").addClass("dynamic-list-hide");
				$(".dynamic-list-add").attr("disabled", "disabled");
			}
		}
	});

	$(".cbarras-autocomplete").on("change keyup paste", function() {
		var cbarras = $(this).val();
		if(materiais != null){
			var estoqueLote = materiais.filter(function(m){ return m.codigos === cbarras; })[0];

		}
		if(estoqueLote) {
			$("#estoque-setor-id").val(estoqueLote.id);
			$(".nome-material").text(estoqueLote.material.nome);
			$(".lote-material").text(estoqueLote.lote);
			$(".validade-material").text(moment(new Date(estoqueLote.validade)).format("DD/MM/YYYY"));
			$(".codigo-interno-material").text(estoqueLote.material.codigoInterno);
			$(".estoque-material").text(estoqueLote.quantidade);
			$(".rounded-info").removeClass("rounded-info-hide");
//			$(".dynamic-list").removeClass("dynamic-list-hide");
			$(".dynamic-list-add").removeAttr("disabled");

		} else {
			$(".rounded-info").addClass("rounded-info-hide");
//			$(".dynamic-list").addClass("dynamic-list-hide");
			$(".dynamic-list-add").attr("disabled", "disabled");
		}
	});

	// Se existir uma lista (página de inclusão de itens)
	if($(".dynamic-list").length > 0){
		var dlIncluirItens = new DynamicList({
			list: ".dynamic-list", 
			element: ".dynamic-list-element",
			elementContainer: ".dynamic-list-container",
			addButton: ".dynamic-list-add",
			removeButton: ".dynamic-list-remove",
			insertAtBegin: true, 
			onAdd: function(callback) {
				
				if(!verifica){
					return ;
				}
				
				var idMaterial = $('#material-id').val();
				var idSaida = $("#saida-id").val();
				var idEstoqueLote = $("#estoque-lote-id").val();
				var idSetor = $("#setor-id").val();
				var token = $("meta[name='_csrf']").attr("content");
				var url = _context + "/saida/api/itemSaida/adicionar";

				var itemSaida = {
						quantidade: $("#quantidade").val(),
						idSaida: idSaida,
						idMaterial: idMaterial,
						idEstoqueLote: idEstoqueLote, 
						idSetor: idSetor
				};
				
				$.ajax({
					url: url,
					type: 'post',
					dataType: 'json',
					data: itemSaida,
					headers: {"X-CSRF-TOKEN":token}, 	   
//					beforeSend: function() {
//					loading.show();
//					}, 
					success: function(response) {
						mf_base.doAlertSet(response.alert);

						if(response.status === "DONE") {
							var item = response.object;
							callback({
								".item-saida-id": {
									value: item.id
								},
								".material": {
									text: item.material.nome
								},  
								".quantidade-disciplina": {
									text: item.quantidade
								},
								".codigo-interno":{
									text: item.material.codigoInterno
								},
								".lote":{
									text: item.lote
								}
							});
						}
					}, 
					error: function(err) {
						mostrarMensagem('Erro ao incluir material! Você está conectado à internet?', "error");
					}, 
					complete: function() {
//						loading.hide();
//						habilitarFinalizar();
					}
				
				});	
			},	
			onRemove: function(itemSaida, callback){
				
				var idItemSaida = itemSaida.find(".item-saida-id").val();
				var quantidadeAtual = parseInt($(".estoque-material").text());
				var quantidadeAdicionada = parseInt(itemSaida.find(".quantidade-setor").text());
				var url = _context +  "/saida/saidaMaterial/remover/"+idItemSaida; 
				
				$.ajax({
					url: url,  
					type: 'get',
					async: false,
					success: function(response){
						var msg = response.object;
						mf_base.doAlertSet(response.alert);
						
					} 
					
				});
				callback();
			}
		});

		var token = $("meta[name='_csrf']").attr("content");
		var url = _context + "/saida/api/itemSaida/listar";
		var saidaMaterial = {
				id: $("#saida-id").val()
		};

		$.ajax({
			url: url,
			type: 'post',
			dataType: 'json',
			data: saidaMaterial,
			headers: {"X-CSRF-TOKEN":token},
			async: false,
			success: function(response) {

				console.log("SUCCESS: " + response);
				if(response.status === "DONE") {

					var items = response.object;

					for(i in items) {

						var item = items[i];
						dlIncluirItens.addItem({
							".item-saida-id": {
								value: item.id
							},
							".material": {
								text: item.material.nome
							},  
							".quantidade-disciplina": {
								text: item.quantidade
							},
							".codigo-interno":{
								text: item.material.codigoInterno
							},
							".lote":{
								text: item.lote
							}
						});
					}
				}
			},
			error: function(err) {
				console.log(err);
				mostrarMensagem("Erro ao carregar os itens desta saída", "error");
			}
		});
	}

});