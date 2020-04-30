$(document).ready( function() {

	finalizar();
	$('.modal-trigger').leanModal();

	$( ".button_finalizar" ).on('click', function(event) {
		console.log("click ajax 1");
		var idEntrada = $(".form_item_entrada").data("id-entrada");	
		var url = "/entrada/finalizarEntrada/" + idEntrada;
		var token = $("meta[name='_csrf']").attr("content");
		$.ajax({
			url: url,
			type: 'post',
			headers: {"X-CSRF-TOKEN":token},
			async: false,
			success: function(data) {

				if(data!="Finalizado"){
					alert(data);
					event.preventDefault();
				}

			},		
		});
	});

	// Formulário da entrada
	$('.datepicker').pickadate({
		selectMonths: true,
		selectYears: 15,
		format: 'dd/mm/yyyy'
	});   

	// Se existir uma lista (página de inclusão de itens)
	if($(".dynamic-list").length > 0){
		var dl = new DynamicList({
			list: ".dynamic-list", 
			element: ".dynamic-list-element",
			elementContainer: ".dynamic-list-container",
			addButton: ".dynamic-list-add",
			removeButton: ".dynamic-list-remove",
			insertAtBegin: true, 
			onAdd: function() {

				var idMaterial = $('#material').find(":selected").val()
				var idEntrada = $(".form_item_entrada").data("id-entrada");
				var token = $("meta[name='_csrf']").attr("content");
				var url = "/entrada/incluirMaterial/" + idEntrada;
				
				var itemEntrada = {
						lote: $('#lote').val(), 
						validade: $('#validade').val(), 
						quantidade: $('#quantidade').val(), 
						valorUnitario: $('#valor-unitario').val(), 
						material: $('#material').find(":selected").val(), 
				};

				var response = $.ajax({
					url: url,
					type: 'post',
					dataType: 'json',
					data: itemEntrada,
					headers: {"X-CSRF-TOKEN":token},
					async: false

				}).responseText;
				if(response === ""){
					Materialize.toast('A soma dos itens excede o valor total da nota!', 4000);
					return false;
				}

				if(response) {
					finalizar();             			
					var item = JSON.parse(response);
					return {
						".material-name": {
							text: $('#material').find(":selected").text()
						}, 
						".material-lote": {
							text: item.lote
						}, 
						".material-quantidade": {
							text: item.quantidade
						}, 
						".material-validade": {
							text: item.validade
						}, 
						".material-valor-unitario": {
							text: item.valorUnitario
						},
						".id-item-material":{
							text : item.id
						}

					};

				}

			}, 
			onAdded: function(_) {
				var total = 0.0;
				$(".dynamic-list-element").each(function(_, el) {
					var e = $(el);
					total += parseFloat(e.find(".material-quantidade").text()) * parseFloat(e.find(".material-valor-unitario").text());
				});
				$(".total-value").text(isNaN(total)? 0.0: total.toFixed(2) );
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
		var url = "/entrada/listarItensMaterial/" + idEntrada;

		var response = $.ajax({
			url: url,
			type: 'post',
			dataType: 'json',
			headers: {"X-CSRF-TOKEN":token},
			async: false

		}).responseText;

		if(response) {

			var items = JSON.parse(response);
			for(k in items) {
				var item = items[k];

				dl.addItem({
					".material-name": {
						text: $('#material').find(":selected").text()
					}, 
					".material-lote": {
						text: item.lote
					}, 
					".material-quantidade": {
						text: item.quantidade
					}, 
					".material-validade": {
						text: item.validade
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

	}// Se existir uma lista (página de inclusão de itens)

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
				onAdd: function() {

					var token = $("meta[name='_csrf']").attr("content");
					var url = "/entrada/alocarItens";

					var alocacaoItemDisciplina = {
							disciplina: $('#disciplina-'+index).find(":selected").val(),
							quantidade: $("#quantidade-"+index).val(),
							itemEntrada: $("#item-entrada-"+index).val(),
							id: null
					};

					var response = $.ajax({
						url: url,
						type: 'post',
						dataType: 'json',
						data: alocacaoItemDisciplina,
						headers: {"X-CSRF-TOKEN":token},
						async: false
					}).responseText;

					if(jQuery.isEmptyObject(response)){
						alert("Todos os itens desse material já foram alocados");
					}else{
						var item = JSON.parse(response);

						$(".itens-alocados-"+index).text(parseInt($(".itens-alocados-"+index).text()) + parseInt(alocacaoItemDisciplina.quantidade));

						return {
							".alocacao-item-disciplina-name": {
								text: item.disciplina.nome
							}, 
							".alocacao-item-quantidade": {
								text: item.quantidade
							},
							".alocacao-item-disciplina-id":{
								value: item.id
							}
						};
					}
					
				},
				onRemove : function(item){
					
					var idItem = item.find(".alocacao-item-disciplina-id").val();
					var quantidade = item.find(".alocacao-item-quantidade").text();
					var url =  "/entrada/alocarItens/remover/"+idItem; 
					var response = $.ajax({
						url: url,  
						type: 'get',
						async: false,
						
					}).responseText;
				
					if(response == "true"){				
						Materialize.toast('Item removido com sucesso!', 3000);
						return true;				
					}else{          						
					  	Materialize.toast('Não foi possível remover esse item!', 3000);
					  	return false;
					}
					var itensAlocados = $("itens-alocados").text();
					
				
				} 
			});

			var token = $("meta[name='_csrf']").attr("content");
			var url = "/entrada/alocarItens/listar";

			var itemEntrada = {
					id: $("#item-entrada-"+index).val()
			};

			var response = $.ajax({
				url: url,
				type: 'post',
				data: itemEntrada,
				dataType: 'json',
				headers: {"X-CSRF-TOKEN":token},
				async: false
			}).responseText;

			var alocacoes = JSON.parse(response);

			var somaItens = 0;

			for(a in alocacoes) {
				var item = alocacoes[a];

				somaItens += item.quantidade;

				dl.addItem({
					".alocacao-item-disciplina-name": {
						text: item.disciplina.nome
					}, 
					".alocacao-item-quantidade": {
						text: item.quantidade
					},
					".alocacao-item-disciplina-id":{
						value: item.id
					}
				});


			}

			$(".itens-alocados-"+index).text(somaItens);
		});
	}// Se existir um conjunto de listas (página de alocação de itens)
	
});

//Validação
function somenteNumeros(num) {
	var er = /[^0-9.,]/;
	er.lastIndex = 0;
	var campo = num;
	if (er.test(campo.value)) {
		campo.value = "";
	}
}


function finalizar(){

	var idEntrada = $(".form_item_entrada").data("id-entrada");
	var url = "/entrada/verificacaoDeFinalizacao/" + idEntrada;
	var token = $("meta[name='_csrf']").attr("content");     	  

	$.ajax({
		url: url,
		type: 'post',
		dataType: 'json',
		headers: {"X-CSRF-TOKEN":token},
		success: function(data) {
			if(data){		
				$("#finalizar").removeClass("disabled");
				$("#finalizar").removeAttr("disabled");
			}else{
				$("#finalizar").addClass("disabled");
				$("#finalizar").attr("disabled","disabled");
			}
		}
	});

}

