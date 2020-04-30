
//jv
function mandarMensagem(elem, msg){
	mf_base.doShowAlert(msg, 'warning', 0, 3000);
	elem.focus();
}


//jv
function mostrarCampos(status) {
	if(status === 'SETOR') {
		$("#div_destino").show();
	}
}


$(document).ready(function() {
	bloquearEnter();
	//inicializando os selects
	$("#tipo").material_select();
	$("#setor-origem").material_select();
	$("#setor-destino").material_select();
	$("#div_destino").hide();

	var status = $("#tipoEditar").text();
	
	mostrarCampos(status);
	
	//data corrente 
	var today = moment()
	var today = today.format('DD/MM/YYYY');
	$('#data').val(today);
	
	
	//mostrar origem quando tipo saida for do tipo setor e a origem for almoxarifado
	$('#setor-origem').on('change', atualizaDestino);
	
	$("#tipo").on('change', atualizaDestino);
	
	function atualizaDestino(){
		if ($("#tipo option:selected").val() === "SETOR" && $("#setor-origem option:selected").val() == "1"){
			$("#div_destino").show();
		}else {
			$("#div_destino").hide();
			$("#div_destino select").val("");
		}
	}
	
	
	
	//jv formSaida
		
	//Validar campos
	$(".formSaida").submit(function (event){
		

		//Checa se a data de chegada é valida
		var now = new Date();
		now.setHours(0,0,0,0);
		var dataChegada = moment($("#data").val(), "DD/MM/YYYY").toDate();

		if(isNaN(dataChegada)){
			mandarMensagem($("#data"), "Data de Saída inválida");
			event.preventDefault();
		}
//		else if(dataChegada > now){
//			mandarMensagem($("#data"), "Data de Saída inválida");
//			event.preventDefault();
//		}	
	});
	
	//jv
	
	
	
	
	
	
	
});

function mostrarErro(msg){
	var alerts = {
			message: msg, 
			type: "error",  
			delayAmount: 0, 
			delay: 3500
	};
	mf_base.doAlertSet(alerts);
}


function valida_form (){
	/*if(document.getElementById("tipo").selectedIndex == 0 ){
		 mostrarErro('Por favor, escolha um tipo de saída front');
		 document.getElementById("tipo").focus();
		 return false;
	 }if(document.getElementById("setor-origem").selectedIndex == 0 ){
			 mostrarErro('Por favor, escolha um setor de origem front');
			 document.getElementById("setor-origem").focus();
			 return false;
			 
	 }*/
}


//
//function verifica(){
//	if(document.getElementById("cbarras")==''){
//		mostrarErro('Por favor, preencha o campo de Material');]
//		document.getElementById("cbarras").focus();
//		return false;
//	}if(documento.getElementById("quantidade")==''){
//		mostrarErro('Por favor, preencha o campo de Material');
//		document.getElementById("quantidade").focus();
//		return false;
//	}
//}

