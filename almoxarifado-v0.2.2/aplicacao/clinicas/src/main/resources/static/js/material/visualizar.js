$(document).ready(function() {
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var estoque;
	var lote;
	var validade;
	var material;
	
	var _context = $("meta[name='_context']").attr("content");
	if(_context == null){
	    _context = "";
	}
	
	$(".edicaoEstoque").on("click", function() {
		$("#modal-editar-estoque").openModal();
		estoque = $(this).attr("estoque");
		lote = $(this).attr("lote");
		validade = $(this).attr("validade");
		material = $(this).attr("material");
		$("#loteEditado").val(lote);
		$("#validadeEditada").val(dataFormatada(validade));
	});
	
	
	$(".confirm-editar-estoque").on("click", function() {
		
		var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
		
		if (!pattern.test($("#validadeEditada").val() )) {
			mandarMensagem($("#validadeEditada"), "Data inválida");
		} else if($("#loteEditado").val() === ''){
			mandarMensagem($("#loteEditado"), "O lote não pode ser vazio");
		}
		else{
			$.ajax({
				url : _context + "/material/"+ material +"/editarEstoqueLote/" + estoque,
				beforeSend : function(request) {
					request.setRequestHeader(header, token);
				},
				type : 'POST',
				data : {
					lote : $("#loteEditado").val(),
					validade: $("#validadeEditada").val() 
				},
				success : function(data) {
					window.location =  _context + "/material/"+ material +"/visualizar";
				}
			});
		}
	});
	
	$(".cancelar-edicao-estoque").on("click", function() {
		$("#modal-editar-estoque").closeModal();
	});
	
});

function mandarMensagem(elem, msg) {
	mf_base.doShowAlert(msg, 'warning', 0, 3000);
	elem.focus();
}

function dataFormatada(dataRecebida){
    var data = new Date(dataRecebida);
    var dia = data.getDate();
    if (dia.toString().length == 1)
      dia = "0"+dia;
    var mes = data.getMonth()+1;
    if (mes.toString().length == 1)
      mes = "0"+mes;
    var ano = data.getFullYear();  
    return dia+"/"+mes+"/"+ano;
}