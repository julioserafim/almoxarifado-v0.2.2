
$(document).ready(function() {
	$("#material-nome").tooltip({
		delay: 50,
		tooltip: $("#material-nome").text()
	});
	$("#material-descricao").tooltip({
		delay: 50,
		tooltip: $("#material-descricao").text()
	});
	
	console.log("chamou");
});