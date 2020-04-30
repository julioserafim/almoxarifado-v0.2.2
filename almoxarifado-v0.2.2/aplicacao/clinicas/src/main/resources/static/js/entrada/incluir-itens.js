function verifica(){
	    var v = document.getElementById("acmaterial");
	    if (v.value == "") {
	    	mostrarMensagem('Por favor, preencha os campos obrigatórios.', "error");
	    	return false;
	    }
	    v = document.getElementById("quantidade");
	    if (v.value == "") {
	    	mostrarMensagem('Por favor, preencha os campos obrigatórios.', "error");
	    	return false;
	    }
	    return true;
	}	

$(document).ready( function() {
	// Mostrando barra de progresso
	var loading = $(".loading").loading({
		type: "linear"
	});
	
	// Se todos os itens já estiverem incluídos,
	// o botão de finalizar deve estar ativado... 
	//habilitarFinalizar();
	
	// Iniciando modal
	$('.modal-trigger').leanModal();
	
	// Iniciando botão de finalização
	$( ".button_finalizar" ).on('click', function(event) {
		var idEntrada = $(".form_item_entrada").data("id-entrada");	
		var url = _context + "/entrada/api/finalizarEntrada/" + idEntrada;
		var token = $("meta[name='_csrf']").attr("content");
		$.ajax({
			url: url,
			type: 'post',
			headers: {"X-CSRF-TOKEN":token},
			async: false,
			success: function(response) {
				console.log(JSON.stringify(response));
				mf_base.doAlertSet(response.alert);
				
				if(response.status === "FAIL") {
					event.preventDefault();
				}

			},		
		});
	});
	
	//cadastro de material
	$('.formMaterial').submit(function(event) {
		
		var formulario = $(this);		
	    var url = _context + "/material/api/adicionar"
	    var token = $("meta[name='_csrf']").attr("content");
	    
	    loading.show();
	    
	    $.ajax({
	        url: url,
	        type: 'post',
	        dataType: 'json',
	        data: formulario.serialize(),
	        headers: {"X-CSRF-TOKEN":token},
	        success: function(response) {
	        	
	        	mf_base.doAlertSet(response.alert);
	        	
	        	if(response.status === "DONE") {
	        		var material = response.object;
	        		
					$('#material')
						.append($("<option select></option>")
						.attr("value", material.id)
						.text(material.nome));
					$('#material').val(material.id);
					$('#material').material_select();
					
					limparFormularioMaterial()
	        	}				
				
	        }, 
	        
	        error: function() {
	        	mostrarMensagem('Erro ao adicionar material! Você está conectado à internet', "error");
	        }, 
	        
	        complete: function() {
	        	loading.hide();
	        }
	    });
	    
	    event.preventDefault();
	    
	});
	 
	$( ".cancelar-material" ).on('click', function(event) {
		limparFormularioMaterial();
	});
 

	// Iniciando lista de materiais incluídos
	var dl = new DynamicList({
		list: ".dynamic-list", 
		element: ".dynamic-list-element",
		elementContainer: ".dynamic-list-container",
		addButton: ".dynamic-list-add",
		removeButton: ".dynamic-list-remove",
		insertAtBegin: true,
		
		// Quando o botão de '+' for clicado
		onAdd: function(callback) {
			if(!verifica()){
				return ;
			}
			var idMaterial = $('#material-id').val()		
			var idEntrada = $(".form_item_entrada").data("id-entrada");
			var token = $("meta[name='_csrf']").attr("content");
			var url = _context + "/entrada/api/incluirMaterial/" + idEntrada;
			
			if ($('#valor-unitario').is(":visible")){
				converteValor('#valor-unitario');
			}
			var itemEntrada = {
				lote: $('#lote').val(), 
				validade: $('#validade').val(), 
				quantidade: $('#quantidade').val(), 
				valorUnitario: $('#valor-unitario').val(), 
				material: $('#material-id').val()
				 
			};
			$.ajax({
		        url: url,
		        type: 'post',
		        dataType: 'json',
		        data: itemEntrada,
		        headers: {"X-CSRF-TOKEN":token}, 	   
		        beforeSend: function() {
		        	loading.show();
		        }, 
				success: function(response) {
					console.log(JSON.stringify(response));
					mf_base.doAlertSet(response.alert);
					
					if(response.status === "DONE") {
						var item = response.object;
						callback({
			    			".material-name": {
			    				text: item.material.nome + " | " + item.material.unidadeMedida.nome
			    			}, 
			    			".material-lote": {
			    				text: item.lote != '' ? item.lote : '-'
			    			}, 
			    			".material-quantidade": {
			    				text: item.quantidade
			    			}, 
			    			".material-validade": {
			    				text: item.validade != null ? moment(new Date(item.validade)).format("DD/MM/YYYY") : '-'
			    			}, 
			    			".material-valor-unitario": {
			    				text: item.valorUnitario
			    			},
			    			".id-item-material":{
			    				text : item.id
			    			}
			    		
			    		});
					} 
					
				}, 

				error: function(err) {
					mostrarMensagem("Erro ao incluir material! Confira os dados informados.", "error");
				}, 
				complete: function() {
					loading.hide();
					//habilitarFinalizar();
				}
		    });			
			
		}, 
		onAdded: function(_) {
			var total = 0.0;
			$(".dynamic-list-element").each(function(_, el) {
				var e = $(el);
				total += parseFloat(e.find(".material-quantidade").text()) * parseFloat(e.find(".material-valor-unitario").text());
			});
			$(".total-value").text(isNaN(total)? 0.0: total.toFixed(2) );
			limparFormularioItem();
			$("#material").material_select();
		}, 
		onRemove: function(item, callback) {
			var total = 0.0;
			var idItem = item.find(".id-item-material").text();				
			var idEntrada = $(".form_item_entrada").data("id-entrada");
			var url = _context + "/entrada/api/" + idEntrada + "/removeritem/" + idItem;           			
			var response = $.ajax({
				url: url,
				beforeSend: function() {
		        	loading.show();
		        },
				success: function(response) {
					mf_base.doAlertSet(response.alert);
					if(response.status === "DONE") {
						// Removing item...
						callback();
					}
				}, 
				error: function(err) {
					mostrarMensagem('Erro ao excluir material! Você está conectado à internet?', "error");
				}, 
				complete: function() {
					loading.hide();
					//habilitarFinalizar();
				}
				
			});
			
		}, 
		onRemoved: function(_) {
			var total = 0.0;
			$(".dynamic-list-element").each(function(_, el) {
				var e = $(el);
				total += parseFloat(e.find(".material-quantidade").text()) * parseFloat(e.find(".material-valor-unitario").text());
			});
			$(".total-value").text(isNaN(total)? 0.0: total.toFixed(2));
			
		}
	});


	var idEntrada = $(".form_item_entrada").data("id-entrada");
	var token = $("meta[name='_csrf']").attr("content");
	var url = _context + "/entrada/api/listarItensMaterial/" + idEntrada;

	$.ajax({
	    url: url,
	    type: 'post',
	    dataType: 'json',
	    headers: {"X-CSRF-TOKEN":token},
	    
	    success: function(response) {
	    	
	    	mf_base.doAlertSet(response.alert);
	    	
	    	if(response.status === "DONE") {
	    		
	 	    	var items = response.object;
				for(k in items) {
					var item = items[k];
					
					dl.addItem({
						".material-name": {
							text: item.material.nome + " | " + item.material.unidadeMedida.nome
						}, 
						".material-lote": {
							text: item.lote != '' ? item.lote : '-'
						}, 
						".material-quantidade": {
							text: item.quantidade
						}, 
						".material-validade": {
							text: item.validade != null ? moment(new Date(item.validade)).format("DD/MM/YYYY") : '-'
						}, 
						".material-valor-unitario": {
							text: item.valorUnitario
						},
						".id-item-material":{
							text : item.id
						}
					});
				}
	    	}
	    }, 
	    error: function(err) {
	    	mostrarMensagem("Erro ao carregar os itens desta entrada", "error");
	    }
	    
	});

	var materiais =[];
	var autocomplete = $(".nome-autocomplete").materialize_autocomplete({
		multiple: {
			enable: false
		},
		cacheable: false,
		getData: function(value, callback){
			
			if(!isNaN(parseFloat(value))){
				var url = _context + "/entrada/api/buscarMaterialCadastrado/" + value;
				var token = $("meta[name='_csrf']").attr("content");
				
				$.ajax({
					dataType: "json",
					url: url,
					type: 'post',
					headers: {"X-CSRF-TOKEN":token},
					success: function(responseMateriais) {
						materiais = responseMateriais.object;
						if(responseMateriais.object != null){
							var data = responseMateriais.object.map(function(m) {
								
								var materiais = m.codigos;
								
								return { id: m.id, text: m.nome  + " | " + m.unidadeMedida.nome};

							});
							callback(value, data);
						}else{
							var data = [];
							callback(value, data);
						}

					}
				}
				);
			}
			if(value.length>3){
				var url = _context + "/entrada/api/buscarMaterialCadastrado/" + value;
				var token = $("meta[name='_csrf']").attr("content");
				
				$.ajax({
					dataType: "json",
					url: url,
					type: 'post',
					headers: {"X-CSRF-TOKEN":token},
					success: function(responseMateriais) {
						materiais = responseMateriais.object;
						if(responseMateriais.object != null){
							var data = responseMateriais.object.map(function(m) {
								
								var materiais = m.codigos;
								
								return { id: m.id, text: m.nome  + " | " + m.unidadeMedida.nome};

							});
							callback(value, data);
						}else{
							var data = [];
							callback(value, data);
						}

					}
				}
				);
			}	
		},
		onSelected: function(item) {
			var id = item.id;
			if(materiais != null){
				var estoqueSetor = materiais.filter(function(m) { return m.id === id; })[0];
			}
			if(estoqueSetor) {
				$("#acmaterial").val(item.text);
				$(".rounded-info").removeClass("rounded-info-hide");
				$("#material-id").val(estoqueSetor.id);
				
			} else {
				$(".rounded-info").addClass("rounded-info-hide");
				$(".dynamic-list-add").attr("disabled", "disabled");
			}
		}
	});

	
});

function getDataFunc(value, callback, context) {

		var url = context + "/entrada/api/buscarMaterialCadastrado/" + value;
		var token = $("meta[name='_csrf']").attr("content");
		
		$.ajax({
			dataType: "json",
			url: url,
			type: 'post',
			headers: {"X-CSRF-TOKEN":token},
			success: function(responseMateriais) {
				materiais = responseMateriais.object;
				if(responseMateriais.object != null){
					var data = responseMateriais.object.map(function(m) {
						
						var materiais = m.codigos;
						
						return { id: m.id, text: m.nome  + " | " + m.unidadeMedida.nome};

					});
					callback(value, data);
				}else{
					var data = [];
					
					callback(value, data);
				}

			}
		}
		);
}

function limparFormularioMaterial(){
	var formulario = $(".formMaterial");		
	formulario.each (function(){
		  this.reset();
		});
	initInputFocus();
}

function limparFormularioFornecedor(){
	var formulario = $(".formFornecedor");		
	formulario.each (function(){
		  this.reset();
		});
	initInputFocus();
}

function limparFormularioItem(){
	var formulario = $(".form_item_entrada");		
	formulario.each (function(){
		  this.reset();
		});
	initInputFocus();
}

/*function habilitarFinalizar(){
		
	var idEntrada = $(".form_item_entrada").data("id-entrada");
	var url = _context + "/entrada/api/verificacaoDeFinalizacao/" + idEntrada;
	var token = $("meta[name='_csrf']").attr("content");     	  

	$.ajax({
		url: url,
		type: 'post',
		dataType: 'json',
		headers: {"X-CSRF-TOKEN":token},
		success: function(response) {
			
			mf_base.doAlertSet(response.alert);
			
			if(response.status === "DONE") {
				if(response.object){		
					$("#finalizar").removeClass("disabled");
					$("#finalizar").removeAttr("disabled");
				} else {
					$("#finalizar").addClass("disabled");
					$("#finalizar").attr("disabled","disabled");
				}
			}
			
		}, 
		error: function(err) {	
			mostrarMensagem('Erro na comunicação com o servidor! Você está conectado à internet?', "error");
		}
	});
	
}*/

function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
}

function somenteNumeros(num) {
    var er = /[^0-9.,]/;
    er.lastIndex = 0;
    var campo = num;
    if (er.test(campo.value)) {
      campo.value = "";
    }
}
