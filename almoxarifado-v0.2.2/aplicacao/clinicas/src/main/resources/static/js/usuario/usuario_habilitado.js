$(document).ready(function() {
	$( ".habilitar" ).click(function(event) {
		event.preventDefault();
		var checkbox = $(this);
		var url = _context + "/usuario/api/habilitar";
		var token = $("meta[name='_csrf']").attr("content");
		var id =$(this).attr("id");
		var hab = $(this).is(':checked');
		var param = {idUsuario : id, habilitar : hab };
		$.ajax({
			url: url,
			type: 'post',
			dataType: 'json',
			data: param,
			headers: {"X-CSRF-TOKEN":token},
			
			 success: function(response) {
				 if(response.status === "DONE"){
					 if(hab){ 
						checkbox.prop('checked', true);
					 }else{
						checkbox.prop('checked', false); 	
					 }
					 
				 }else{
					 mf_base.doAlertSet(response.alert);
				 }
				 
			 },
			 
			 error: function(response) {
				 mf_base.doAlertSet({
                     message: "Não há conexão", 
                     type: "error", 
                     delay: 3000
                 });
			}
		
		});
		
	});
	
});