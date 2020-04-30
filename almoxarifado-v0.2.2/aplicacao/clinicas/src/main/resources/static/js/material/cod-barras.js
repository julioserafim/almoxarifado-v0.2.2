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
			var idMaterial = $("#idMaterial").val();
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
					mostrarMensagem("Erro ao excluir Código de Barras! Você está conectado à internet?", "error");
				}
			});
		}

	});

	function adicionarCodigos(){
		var idMaterial = $("#idMaterial").val();
		var url =   _context + "/material/api/" + idMaterial +"/buscarCodigoBarras" ;
		var token = $("meta[name='_csrf']").attr("content");
		$.ajax({
			url: url,
			type: "get",
			dataType: "json",
			headers: {"X-CSRF-TOKEN":token},
			success: function(response) {
				var codigoRetornado = response.object;
				codigoRetornado.map(function(c) {
					dlBarCode.addItem({
						".bar-code": { text: c.codigo 
						}
					});
				});

			}

		});
	}
	adicionarCodigos();

});



function valida_form(){
	 $("#unidade_medida").removeAttr("disabled");
}


