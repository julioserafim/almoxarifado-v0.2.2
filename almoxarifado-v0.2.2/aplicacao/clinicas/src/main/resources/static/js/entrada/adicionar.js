function setarCampos(){
	var classe = $("select#tipo option").filter(":selected").val();
	$(".esconder").hide();
	//$(".esconder").find("input[type=text]").val("");
	$('#setor-nome').material_select();
	$('#fornecedor-nome').material_select();
	$("."+classe).show();
	
	if($("#tipo").val() == 'AJUSTE_ESTOQUE' || $("#tipo").val() == 'DEVOLUCAO' || $("#tipo").val() == 'OUTROS' || $("#tipo").val() == 'DOACAO') {
		$("#valorNotaFiscal").prop("required", false);
	} else {
		$("#valorNotaFiscal").prop("required", true);
	}
}

function mostrarCampos(status) {
	switch(status) {
		case 'FORNECEDOR':
			$(".FORNECEDOR").removeClass('esconder');
			break;
		case 'DEVOLUÇÃO':
			$(".DEVOLUCAO").removeClass('esconder');
			break;
	}
}

function mandarMensagem(elem, msg){
	mf_base.doShowAlert(msg, 'warning', 0, 3000);
	elem.focus();
}

$(document).ready( function() {
	var status = $("#tipoEditar").text();
	
	mostrarCampos(status);
	
	bloquearEnter();
	
	$(".modal-trigger").leanModal({
		dismissible: false
	});
	
	//CAMPOS QUE APARECEM DE ACORDO COM A OPÇÃO SELECIONADA
	setarCampos();
	$("#tipo").change(setarCampos);
	
	//Validar campos
	$(".formEntrada").submit(function (event){
		// remover os pontos e trocando a virgula por ponto, o unmask perde as casas decimais 
		converteValor('#valorNotaFiscal');

//		//Checa se há campos nulos
//		var inputs = $(".formEntrada").find("input[type=text]").filter(":visible").filter('.obrigatorio');
//		for (i = 0; i < inputs.length; i++){
//			val = inputs[i].value;
//			if (val == null || val == '' || val == 'Selecione um Fornecedor' || val == 'Selecione um Setor de Origem'){
//				mandarMensagem(inputs[i], "Preencha todos os campos.");
//				inputs[i].scrollIntoView();
//				event.preventDefault();
//				return;
//			}
//		}
		//Checa se a data de chegada é valida
		var now = new Date();
		now.setHours(0,0,0,0);
		var dataChegada = moment($("#chegada").val(), "DD/MM/YYYY").toDate();

		if(isNaN(dataChegada)){
			mandarMensagem($("#chegada"), "Data de chegada inválida");
			event.preventDefault();
		}else if(dataChegada > now){
			mandarMensagem($("#chegada"), "Data de chegada inválida");
			event.preventDefault();
		}	
	});
	
	//cadastro de fornecedor
	$('.formFornecedor').submit(function(event) {

		var formulario = $(this);
		var url = _context + "/fornecedor/api/adicionar";
		var token = $("meta[name='_csrf']").attr("content");
		
		var loading = $(".loading").loading({
			type: "linear"
		});

		$.ajax({
			url: url,
			type: 'post',
			dataType: 'json',
			data: formulario.serialize(),
			headers: {"X-CSRF-TOKEN":token},
			
			beforeSend: function() {
	        	loading.show();
	        }, 
			success: function(response) {
				
				mf_base.doAlertSet(response.alert);
				
				if(response.status === 'DONE') {
					
					fornecedor = response.object;
					
					$('#fornecedor')
						.append($("<option select></option>")
						.attr("value", fornecedor.id)
						.text(fornecedor.razaoSocial));
					$('#fornecedor').val(fornecedor.id);
					$('#fornecedor').material_select();

					limparFormularioFornecedor()
				}
				
			}, 
			error: function() {
				mostrarMensagem("Erro ao inserir fornecedor!", "error");
				loading.finish();
			}, 			
			complete: function() {
				loading.hide();
			}
		});
		
		event.preventDefault();
	});
	
	$( ".cancelar-fornecedor" ).on('click', function(event) {
		limparFormularioFornecedor();
	});

});


function limparFormularioFornecedor(){
	var formulario = $(".formFornecedor");		
	formulario.each (function(){
		  this.reset();
		});
}



$(".datepicker").pickadate({
    selectMonths: true,
    selectYears: 15,
    format: 'dd/mm/yyyy'
});   

