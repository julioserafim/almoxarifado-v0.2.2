$(document).ready(function(){
	 
	 /*
		 * Código de Barras
		 */
	 
	
	 
	 var dlBarCode = new DynamicList({
	    	list: ".dl", 
	    	element: ".dl-el",
	    	elementContainer: ".dl-container",
	    	addButton: ".dl-add",
	    	removeButton: ".dl-delete",
	    	insertAtBegin: true,
	    	onAdd: function(callback) {
	    		
	    		Materialize.updateTextFields();
	    		
	    	}, 
	    	onRemove: function(item, callback) {
				
				var idCodigo = item.find(".bar-code").text();
				var idMaterial = $("#id").val();
				var url = _context + "/material/" + idMaterial + "/removerCodigoBarras/" + idCodigo; 
				
				var response = $.ajax({
					url: url,
					success: function(response) {
						mf_base.doAlertSet(response.alert);
						if(response.status === "DONE") {
							// Removing item...
							callback();
						}
					}, 
					error: function(err) {
						mostrarMensagem('Erro ao excluir Código de Barras! Você está conectado à internet?', "error");
					}
				});
			}

	 });
	
	 /*
		 * Fim Código de Barras
		 */
	 
	 
	/*
	 * Auto Complete
	 */
	
	 
	var itemTemplate = $(".horizontal-list").find("#material-item-template");
	itemTemplate.attr("data-id", "<%= item.id %>");
	itemTemplate.attr("data-text", "<%= item.text %>");
	itemTemplate.find(".val-nome-material").text("<%= item.text %>");
	itemTemplate.find(".val-id-material").val("<%= item.id %>");
	//itemTemplate.find(".val-codigos").text("<%= item.cBarras %>");
//	itemTemplate.find(".val-codigo-interno-material").text("<%= item.cInterno %>");
	itemTemplate.find(".val-unidade-medida").text("<%= item.unidadeMedida %>");
	
	itemTemplate = $(".horizontal-list").html();
	itemTemplate = itemTemplate.replace(new RegExp("&lt;", "g"), "<").replace(new RegExp("&gt;", "g"), ">");
	
	$(".horizontal-list").text("");
	
	var __materiaisFilteredByName = [];
	
	var __getMaterialById = function(id) {
		return __materiaisFilteredByName.filter(function(m) {
    		return m.id == id;
    	})[0] || false;
	}
	var __getMaterialByName = function(name) {
		name = name.replace(/ +(?= )/g,'');
		return __materiaisFilteredByName.filter(function(m) {
    		return m.nome == name;
    	})[0] || false;
	}
	var __enableInputs = function() {
		
		$('input[name="descricao"').val("");
		$('input[name="descricao"').removeAttr("disabled");
		
		$('select[name="unidadeMedida"').removeAttr("disabled");
		$('select[name="unidadeMedida"').material_select();
		
		$('select[name="grupo"').val("");
		$('select[name="grupo"').removeAttr("disabled");
		$('select[name="grupo"').material_select();
		
		$('input[name="id"').val(null);
		
		$('input[name="codigoInterno"').val("");
		$('input[name="codigoInterno"').removeAttr("disabled");

		
	}
	var __unableInputs = function(material) {
		
		$('select[name="unidadeMedida"').val(material.unidadeMedida? material.unidadeMedida.id: null).change();
		$('select[name="unidadeMedida"').attr("disabled", "");
		$('select[name="unidadeMedida"').material_select();
		
		$('input[name="descricao"').val(material.descricao);
		$('input[name="descricao"').attr("disabled","");
		
		$('select[name="grupo"').val(material.grupo? material.grupo.id: null).change();
		$('select[name="grupo"').attr("disabled", "");
		$('select[name="grupo"').material_select();
		
		$('input[name="id"').val(material.id);
		
		$('input[name="codigoInterno"').val(material.codigoInterno);
		$('input[name="codigoInterno"').attr("disabled", "");
		Materialize.updateTextFields();
	}
	
	$('input[name="nome"').on("change paste keyup", function(e) {
		var material = __getMaterialByName($(this).val());
		__enableInputs();
			
	});
	
	
	var autocomplete = $('#name').materialize_autocomplete({
	    multiple: { enable: false },
	    cacheable: false, 
	    getData: function (value, callback) {
	    	
	    	$(".codBarrasList").remove();
	    	value = value.replace(/^\s*/g, '').replace(/\s*$/g, '');
	    	if(!value || value == "") {
	    		__enableInputs();
	    		return;
	    	}
	    	
	    	if(value.length > 3) {
		    	
		    	var url = _context + "/material/api/buscarPorNome/material/" + value;
				var token = $("meta[name='_csrf']").attr("content");
				$.ajax({
					url: url,
					type: 'post',
					dataType: 'json',
					headers: {"X-CSRF-TOKEN":token},
					success: function(response) {
						
						__enableInputs();
						if(response.status === "DONE") {
	
							data = response.object.map(function(m) {
								var uMedida = "";
								if (m.unidadeMedida != null) {
									uMedida = m.unidadeMedida.nome;
								}
								
								return { id: m.id, text: m.nome, cBarras: m.codigos, cInterno: m.codigoInterno, desc:m.descricao,  unidadeMedida: uMedida};
							});
							
							callback(value, data);
							__materiaisFilteredByName = response.object; 
								
						} else {
							
							console.log("Erro ao buscar materiais");
							
						}
			    		
					}, 
					error: function(err) {	
						
						console.log("Erro ao buscar materiais: " + err);
						
					}
				});
	    	}
	    }, 
	    dropdown: {
	    	el: $(".horizontal-list"), 
	    	itemTemplate: itemTemplate 
	    }, 
	    onListShow: function() {
	    	console.log("List Show");
	    }, 
	    
	    onListHide: function() {
	    	console.log("List Hide");
	    }, 
	    onSelected: function(item) {
	    	
	    	var material = __getMaterialById(item.id);
	    	var idMaterial = item.id;
	    	var url =   _context + "/material/api/" + idMaterial +"/buscarCodigoBarras" ;
	    	var token = $("meta[name='_csrf']").attr("content");
	    	$.ajax({
				url: url,
				type: 'get',
				dataType: 'json',
				headers: {"X-CSRF-TOKEN":token},
				success: function(response) {
					
					var materialRetornado = response.object.material;
					var codigoRetornado = response.object;
					
					__unableInputs(material);
					
		    		
		    		// ...
		    		
		    		
		    		//this.dlBarCode.clear();
		    			codigoRetornado.map(function(c) {
			    			dlBarCode.addItem({
			    				".bar-code": { text: c.codigo 
			    				}
			    			});
			    		});
					
					}
		    		
				});
	    	
	    },limit:10
	    	
	    	
	    
	    
	});
	
	$('#name').focusout(function(){
		/*
		 * setTimeout(function() {			
			autocomplete.hide();
		}, 100);
		*/
	});
	
	/*
	 * Fim Auto Complete
	 */
	
	
	
	// esta função de excluir está em conflito com a outra função de excluir do
	// clinicas.js
	/*
	 * $('.bt-excluir').click(function(event){ var url =
	 * $(this).attr('data-url'); $('#confirm-excluir').attr('href',url);
	 * $('#modal-excluir').openModal(); });
	 */
	var dataTable = mf_base.doAddDataTable($("#table-material"));
	
	
	$('.material-detail').click(function(){
		var url = $(this).data("url");
    	$.get(url, function(data){
    		$("#modal-material-detail span[data-nome]").html(data.object.material.nome);
    		$("#modal-material-detail span[data-codigo-interno]").html(data.object.material.codigoInterno);
    		$("#modal-material-detail span[data-codigo-barras]").html(data.object.material.codigoBarras);
    		$("#modal-material-detail span[data-unidade-medida]").html(data.object.material.unidadeMedida.nome);
    		$("#modal-material-detail span[data-estoque]").html(data.object.material.estoque);
    		$("#modal-material-detail span[data-grupo]").html(data.object.material.grupo.nome);
    
    		var estoque_setores =  $("#estoque-setores tbody");
    		$.each(data.object.estoque_por_setores, function(index, value){
    			var tr = $('<tr>');
    			tr.append($('<td>').text(value.setor.nome));
    			tr.append($('<td>').text(value.quantidade));
    			estoque_setores.append(tr);
    		});
    		$("#modal-material-detail").openModal({
        		complete: function(){
    				var estoque_setores = $("#estoque-setores tbody");
    				estoque_setores.empty();
    			}
    		});
    	});
	});
	
	
	
});	
 


function valida_form (){
	 $("#unidade_medida").removeAttr("disabled");
}
