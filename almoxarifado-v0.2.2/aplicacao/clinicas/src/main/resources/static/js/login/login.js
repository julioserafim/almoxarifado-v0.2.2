$(document).ready(function() {	
	var verifyLogin = function(clearPassword) {
		
		// Limpando o campo da senha
		if(clearPassword === true) {
			$("#password").val("");
			$("#password").addClass("white");
		}
		
		if($("#password").val().length > 0)
			Materialize.updateTextFields();
		
	};
	
	$("#username").keyup(verifyLogin);
	$("#password").keyup(verifyLogin);
	
	// Necessário devido a impossibilidade de detectar o autofill de todos os browsers
	setTimeout(function() { verifyLogin(true); }, 100);
	setTimeout(function() { verifyLogin(true); }, 200);
	setTimeout(function() { verifyLogin(true); }, 400);
	
});