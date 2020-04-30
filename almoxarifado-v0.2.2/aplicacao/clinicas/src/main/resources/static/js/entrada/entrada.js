$(document).ready( function() {
	if($(".incluirItens").exists())
		finalizar();
	
	$('.modal-trigger').leanModal();
	
	$( ".button_finalizar" ).on('click', function(event) {
		console.log("click ajax 1");
		var idEntrada = $(".form_item_entrada").data("id-entrada");	
		var url =_context + "api/entrada/finalizarEntrada/" + idEntrada;
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
	
	//cadastro de material
	$('.formMaterial').submit(function(event) {
		
		var formulario = $(this);		
	    var url = _context + "/material/api/adicionarMaterial"
	    console.log("add material");
	    var token = $("meta[name='_csrf']").attr("content");
	    
	    $.ajax({
	        url: url,
	        type: 'post',
	        dataType: 'json',
	        data: formulario.serialize(),
	        headers: {"X-CSRF-TOKEN":token},
	        success: function(data) {
	        	console.log(data.id);
	        	console.log(data.nome);
	        	 $('#material')
	             .append($("<option select></option>")
	             .attr("value",data.id)
	             .text(data.nome));
	        	 $('#material').val(data.id);
	        	 $('#material').material_select();
	        	 
	        	 //reset formulário
	        	 limparFormularioMaterial()
	        	
	        }
	    });
	    event.preventDefault();
	});
	
	//reset formulário material 
	$( ".cancelar-material" ).on('click', function(event) {
		limparFormularioMaterial();
	});
	
	//cadastro de fornecedor
	$('.formFornecedor').submit(function(event) {

		var formulario = $(this);		
		var url = _context + "/fornecedor/api/adicionarFornecdor"
			console.log("add fornecedor");
		var token = $("meta[name='_csrf']").attr("content");

		$.ajax({
			url: url,
			type: 'post',
			dataType: 'json',
			data: formulario.serialize(),
			headers: {"X-CSRF-TOKEN":token},
			success: function(data) {
				console.log(data.id);
				console.log(data.razaoSocial);
//	        	mf_base.doAlertSet(response.alert);
	        	alert("dsadsa");
				$('#fornecedor-nome')
				   .append($("<option select></option>")
						.attr("value",data.id)
						.text(data.razaoSocial));
				$('#fornecedor-nome').val(data.id);
				$('#fornecedor-nome').material_select();

				//reset formulário
				limparFormularioFornecedor()

			}
		});
		event.preventDefault();
	});
	
	//reset formulário fornecedor
	$( ".cancelar-fornecedor" ).on('click', function(event) {
		limparFormularioFornecedor();
	});


});

function limparFormularioMaterial(){
	var formulario = $(".formMaterial");		
	formulario.each (function(){
		  this.reset();
		});
}

function limparFormularioFornecedor(){
	var formulario = $(".formFornecedor");		
	formulario.each (function(){
		  this.reset();
		});
}

function finalizar(){
		
	var idEntrada = $(".form_item_entrada").data("id-entrada");
	var url = _context + "api/entrada/verificacaoDeFinalizacao/" + idEntrada;
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


//DynamicList, add
$('.datepicker').pickadate({
    selectMonths: true, // Creates a dropdown to control month
    selectYears: 15,
    format: 'dd/mm/yyyy'
});   

var dl = new DynamicList({
	list: ".dynamic-list", 
	element: ".dynamic-list-element",
	elementContainer: ".dynamic-list-container",
	addButton: ".dynamic-list-add",
	removeButton: ".dynamic-list-remove",
	insertAtBegin: true, 
	onAdd: function() {
		
		/* Mostrando barra de progresso */
		var loading = null;
		setTimeout(function() {
			loading = $(".loading").loading({
				type: "linear"
			});
		}, 0);
		
		var idMaterial = $('#material').find(":selected").val()
		var idEntrada = $(".form_item_entrada").data("id-entrada");
		var token = $("meta[name='_csrf']").attr("content");
		var url = _context + "/entrada/incluirMaterial/" + idEntrada;
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
		
		setTimeout(function() {
			loading.finish();
		}, 500);
		
		if(response === ""){
			 mostrarMensagem('A soma dos itens excede o valor total da nota!', "error");
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
    				text: moment(new Date(item.validade)).format("DD/MM/YYYY")
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
	onRemove: function(item) {
		var total = 0.0;
		var idItem = item.find(".id-item-material").text();				
		var idEntrada = $(".form_item_entrada").data("id-entrada");
		var url = _context + "/entrada/"+idEntrada+"/removeritem/"+idItem;           			
		var response = $.ajax({
			url: url,              					
			async: false
		}).responseText;
		console.log(response);
		if(response == "true"){				
			return true;				
		}else{          						
			mostrarMensagem('Não foi possível remover esse item!', "error");
		  	return false;
		}          													
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
var url = _context + "api/entrada/listarItensMaterial/" + idEntrada;

var response = $.ajax({
    url: url,
    type: 'post',
    dataType: 'json',
    headers: {"X-CSRF-TOKEN":token},
    async: false
    
}).responseText;
console.log(response);
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
				text: moment(new Date(item.validade)).format("DD/MM/YYYY")
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

//Validação
function somenteNumeros(num) {
    var er = /[^0-9.,]/;
    er.lastIndex = 0;
    var campo = num;
    if (er.test(campo.value)) {
      campo.value = "";
    }
}

