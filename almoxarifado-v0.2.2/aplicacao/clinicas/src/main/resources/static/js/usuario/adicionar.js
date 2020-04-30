$(document).ready( function() {
	$(".formEntrada").submit(function (event){
		$("#valorNotaFiscal").unmask();
		//Checa se há campos nulos
		var inputs = $(".formEntrada").find("input[type=text]").filter(":visible");
		for (i = 0; i < inputs.length; i++){
			val = inputs[i].value;
			if (val == null || val == '' || val == 'Selecione um Fornecedor' || val == 'Selecione um Setor de Origem'){
				mandarMensagem(inputs[i], "Preencha todos os campos.");
				inputs[i].scrollIntoView();
				event.preventDefault();
				return;
			}
		}
		//Checa se a data de chegada é valida
		var now = new Date();
		now.setHours(0,0,0,0);
		var dataChegada = moment($("#chegada").val(), "DD/MM/YYYY").toDate();
		if(dataChegada > now){
			mandarMensagem($("#chegada"), "Data de chegada inválida");
			event.preventDefault();
		}

	});
}
