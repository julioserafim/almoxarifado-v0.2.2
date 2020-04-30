$(document).ready(function() {
	$('.search-result-container').hide();
	$("#search-bar").click(function(){
		$(this).val("");
	});
	
	var materiais = [];
	var autocomplete = $(".search-bar-autocomplete").materialize_autocomplete({
		multiple: {
			enable: false
		},
		cacheable: false,
		getData: function (value, callback) {
			var url = _context + "/material/api/buscarPorNome/material/" + value;
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
							return { id: m.id, text: m.id +" | "+m.nome};
						});
						callback(value, data);
					}else{
						var data = [];
						callback(value, data);
					}

				}
			});
		}, 
		onSelected: function(item) {
			var id = item.id;
			if(materiais != null){
				item = materiais.filter(function(m) { return m.id === id; })[0];
			}
			
			if(item) {
				$("#nome-material").text(item.nome);
				$("#codigo-interno").text(item.codigoInterno);
				$("#unidade-medida").text(item.unidadeMedida.nome);
				$("#estoque").text(item.estoque);
				$("#grupo").text(item.grupo.nome);
				$('.search-result-container').show();
				$("#material-id").val(item.id);
				document.getElementById('edit-material').setAttribute('href', '/material/editar/'+item.id);
				document.getElementById('#remove-material').setAttribute('href', '/material/remover/'+item.id);
			} else {
				$('.search-result-container').hide();
			}
		}
	});

	$(".search-bar-autocomplete").on("change keyup paste", function() {
		var cbarras = $(this).val();
		if(materiais != null){
			var item = materiais.filter(function(m){ 
				for (c in m.codigos)
					if (c === cbarras)
						return true;
				return false;
			})[0];
		}
		if(item) {
			$("#nome-material").text(item.nome);
			$("#codigo-interno").text(item.codigoInterno);
			$("#unidade-medida").text(item.unidadeMedida.nome);
			$("#estoque").text(item.estoque);
			$("#grupo").text(item.grupo.nome);
			$('.search-result-container').show();
			$("#material-id").val(item.id);
			document.getElementById('#edit-material').setAttribute('href', '/material/remover/'+item.id);
		} else {
			$('.search-result-container').hide();
		}
	});

	
});