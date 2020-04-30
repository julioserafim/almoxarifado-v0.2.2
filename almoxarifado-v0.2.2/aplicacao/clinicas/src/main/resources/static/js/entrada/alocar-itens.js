

$(document).ready( function() {
	
	// Mostrando barra de progresso
	var loading = $(".loading").loading({
		type: "linear"
	}); 

	$("#btn-finalizar-alocacao").on('click', function(event){
		event.preventDefault();
		verificarFinalizacaoAlocacao();
		if($(this).hasClass("disabled")){
			return false;
		}else{
			window.location = $(this).attr("href");
		}
	});
	
	//	Remover botão quando alocação de item especifico estiver completa
	function botaoAlocacao(index, qtd){
		if (parseInt(qtd) + parseInt($(".itens-alocados-"+index).text()) == parseInt($(".qtd-total-"+index).val()) ){			
			$(".dynamic-list-add-"+index).hide();
			$(".dynamic-list-add-"+index).parent().parent().children().filter(".collapsible-header").addClass("light-green lighten-2");
			
			console.log($(".dynamic-list-add-"+index).parent().parent().children().filter(".collapsible-header"));
		}else{
			$(".dynamic-list-add-"+index).parent().parent().children().filter(".collapsible-header").removeClass("light-green lighten-2");
			$(".dynamic-list-add-"+index).show();
		}
	};
	
	function verificarFinalizacaoAlocacao(){
		var idEntrada = $(".entrada-id").val();
		var url = _context +  "/entrada/api/"+idEntrada+"/verificarFinalizacaoAlocacao";
		var token = $("meta[name='_csrf']").attr("content");
		$.ajax({
			url: url,
			type: 'get',
			dataType: 'json',
			headers: {"X-CSRF-TOKEN":token},
			async: false,
			success: function(response) {
				
				if(response.status === "DONE") {
					if(response.object){
						mf_base.doAlertSet(response.alert);
						$("#btn-finalizar-alocacao").removeClass("disabled");
						$("#btn-finalizar-alocacao").removeAttr("disabled");
						var elArray  = $("*[class*='dynamic-list-add-']");

						for (var a= 0; a < elArray .length; a++){
							$(elArray[a]).hide();
						}
						
					}else{
						$("#btn-finalizar-alocacao").addClass("disabled");
						$("#btn-finalizar-alocacao").attr("disabled");
					}
				}
			}, 
			error: function(err) {	
				console.log(err);
				mostrarMensagem('Erro na comunicação com o servidor! Você está conectado à internet?', "error");
			}
		});
	};
	
	// Se existir um conjunto de listas (página de alocação de itens)
	if($(".dynamic-list-0").length > 0){
		$(".collapsible li").each(function(index, el) {
			
			var dl = new DynamicList({
				list: ".dynamic-list-" + index, 
				element: ".dynamic-list-element-" + index,
				elementContainer: ".dynamic-list-container-" + index,
				addButton: ".dynamic-list-add-" + index,
				removeButton: ".dynamic-list-remove-" + index,
				insertAtBegin: true, 
				onAdd: function(callback) {
					if($('#setor-'+index).val() == -1){
						mostrarMensagem("Por favor informe o setor","error");
						return ;
					}if($('#quantidade-'+index).val()==''){
						mostrarMensagem("Por favor informe a quantidade","error");
						return ;
					}
					
					var token = $("meta[name='_csrf']").attr("content");
					var url = _context + "/entrada/api/alocarItens";

					var alocacaoItemSetor = {
							setor: $('#setor-'+index).find(":selected").val(),
							quantidade: $("#quantidade-"+index).val(),
							itemEntrada: $("#item-entrada-"+index).val(),
							id: null
					};

					$.ajax({
						url: url,
						type: 'post',
						dataType: 'json',
						data: alocacaoItemSetor,
						headers: {"X-CSRF-TOKEN":token},
						async: false,
				        success: function(response) {
				        	
							mf_base.doAlertSet(response.alert);
							
							if(response.status === "DONE") {
								
								verificarFinalizacaoAlocacao();
								
								var item = response.object;
								botaoAlocacao(index, alocacaoItemSetor.quantidade);
//								Atualização da quantidade de itens alocados do material no cabeçalho
								$(".itens-alocados-"+index).text(parseInt($(".itens-alocados-"+index).text()) + parseInt(alocacaoItemSetor.quantidade));
								callback({
									".alocacao-item-setor-nome": {
										text: item.setor.nome
									}, 
									".alocacao-item-quantidade": {
										text: item.quantidade
									},
									".alocacao-item-setor-id":{
										value: item.id
									}
					    		});
							}
				        },
						error: function(err) {
							mostrarMensagem('Erro ao incluir material! Você está conectado à internet?', "error");
						}
					});
				},		
				onRemove : function(item, callback) {
					
					var idItem = item.find(".alocacao-item-setor-id").val();
					var quantidade = item.find(".alocacao-item-quantidade").text();
					var quantidadeAtual = $(".itens-alocados-"+index).text();
					var url = _context + "/entrada/api/alocarItens/remover/"+idItem; 
					
					var response = $.ajax({
						url: url,  
						type: 'get',
						async: false
						
					}).responseText;
				
					if(response == "true"){				
						
						mostrarMensagem('Item removido com sucesso!', "success");
						$(".itens-alocados-"+index).text(quantidadeAtual - quantidade);
						$("#btn-finalizar-alocacao").addClass("disabled");
						botaoAlocacao(index, 0);
						callback();
						return true;				
					} else {          						
					  	
						mostrarMensagem('Não foi possível remover esse item!', "error");
					  	return false;
					}
					
					
					
					
				}, 
				onAdded:function(item) {
					console.log("added");
					botaoAlocacao(index, 0);
				}
				
			});

			var token = $("meta[name='_csrf']").attr("content");
			var url = _context + "/entrada/api/alocarItens/listar";
			var itemEntrada = {
					id: $("#item-entrada-"+index).val()
			};
			
			$.ajax({
				url: url,
				type: 'post',
				dataType: 'json',
				data: itemEntrada,
				headers: {"X-CSRF-TOKEN":token},
				async: false,
				success: function(response) {
													    	
			    	if(response.status === "DONE") {
			    		
			    		var somaItens = 0;
			 	    	var items = response.object;
			 	    	
			 	    	console.log(JSON.stringify(response));
			 	    							
			 	    	for(i in items) {
							
							var item = items[i];
							somaItens += item.quantidade;
														
							dl.addItem({
								".alocacao-item-setor-nome": {
									text: item.setor.nome
								}, 
								".alocacao-item-quantidade": {
									text: item.quantidade
								},
								".alocacao-item-setor-id":{
									value: item.id
								}
							});
						}
			 	    	
			 	    	$(".itens-alocados-"+index).text(somaItens);
			    	}
					
				},
				
			    error: function(err) {
			    	mostrarMensagem("Erro ao carregar os itens desta entrada", "error");
			    }
			});
			
		});
		
		verificarFinalizacaoAlocacao();
		
	}// Se existir um conjunto de listas (página de alocação de itens)

});