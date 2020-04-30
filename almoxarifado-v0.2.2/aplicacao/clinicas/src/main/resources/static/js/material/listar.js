$(document).ready(function() {
	
	$("#table-filter").focus();
	
	$(".modal-trigger").click(function() {
		var url = $(this).data("url");
		var token = $("meta[name='_csrf']").attr("content");
		$.ajax({
			url: url,
			type: 'get',
			dataType: 'json',
			headers: {"X-CSRF-TOKEN":token},
			async: true,
			success: function(response) {
				
				if(response.status === "DONE") {
				
					if(response.object){
						mf_base.doAlertSet(response.alert);
						var material = response.object.material;
						for(k in material) {
							if(k == "codigos")
								$("#modal-material-detail ." + k).text(material[k].length? material[k].join(", ") : "-");
							else
								$("#modal-material-detail ." + k).text(material[k]? material[k] : "-");
						}
						
						$($(".modal-trigger").attr("href")).openModal();
						
					} else {
						console.log("Erro");
					}
				}
			}, 
			error: function(err) {	
				console.log(err);
				mostrarMensagem('Erro na comunicação com o servidor! Você está conectado à internet?', "error");
			}
		});
	});
	
	$('body').on('keyup', '.busca-material-autocomplete', function(){
			if($(this).val().length > 2){
				var url = _context + "/material/api/buscarPorNome/material/" + $(this).val();
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
							pupularListaResultado(data);
						}else{
							var data = [];
							pupularListaResultado(data);
						}
					}
				}
				);
			}	
		}
	);
	
});


function pupularListaResultado(data){
	$("ul.resultado-busca-material").empty();
	$.each(data, function (index, value) {
		$("ul.resultado-busca-material").append('<li><a href="'+_context+'/material/'+value.id+'/visualizar">'+value.text+'</a></li>');
	});
}