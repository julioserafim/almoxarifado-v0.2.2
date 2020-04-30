$(function(){
	bloquearEnter();
	$(".modal-trigger").leanModal();
		
	/* Para identificar o submit clicado */
	$("form button[type=submit]").click(function() {
        $("button[type=submit]", $(this).parents("form")).removeAttr("clicked");
        $(this).attr("clicked", "true");
    });
	
	$('.form_ajax').submit(function(event,data) {
		
		var formulario = $(this);
		var model = formulario.attr('data-model');
		var acao = formulario.attr('data-action');
	    var url = _context + "/" + model + "/api/" + acao;
	    var redirectAction = $("button[type=submit][clicked=true]").val();
	    console.log(redirectAction);
	    
	    var token = $("meta[name='_csrf']").attr("content");
	    console.log(formulario.serialize());
	    $.ajax({
	        url: url,
	        type: 'post',
	        dataType: 'json',
	        data: formulario.serialize(),
	        headers: {"X-CSRF-TOKEN":token},
	        success: function(data) {
	        	if(data.status === "DONE"){

	        		if(redirectAction === "keep_add") {
	        			setTimeout(function(){
	        				window.location = _context + "/" + model + "/adicionar";
	        			}, 500);
	        			
	        		} else {
	        			setTimeout(function(){
	        				window.location = _context + "/" + model + "/listar";
	        			}, 500);
	        			
	        		}	        		

	        	} else if(data.status === "FAIL") { }
	        		
	        	mf_base.doAlertSet(data.alert);
	        }	        
	    });
	    
	    event.preventDefault();
	    
	});
	
	$('.bt-excluir').click(function(event){
		var url = $(this).attr('data-url');	
		var tr = $(this).closest('tr');
		tr.attr("id", "atual");
		$('#confirm-excluir').attr('href',url);
		$('#modal-excluir').openModal({
			complete: function() { $("tr#atual").attr("id", "#"); }
		});
	});
	
	$('#confirm-excluir').on('click', function(event){
		event.preventDefault();
		var url = $(this).attr('href');	
		$.get(url, function(data){
			if(data.status === "DONE"){
				$('tr#atual').remove();
			}
			mf_base.doAlertSet(data.alert);
		});
	});
});
