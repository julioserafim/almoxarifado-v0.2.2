function converteValor(elem){
	var valor = $(elem).val().replace(/\./g, '').replace(',', '.');
	console.log(valor);
	$(elem).val(valor);
}

function redirect (url) {
    var ua        = navigator.userAgent.toLowerCase(),
        isIE      = ua.indexOf('msie') !== -1,
        version   = parseInt(ua.substr(4, 2), 10);

    // Internet Explorer 8 and lower
    if (isIE && version < 9) {
        var link = document.createElement('a');
        link.href = url;
        document.body.appendChild(link);
        link.click();
    }

    // All other browsers can use the standard window.location.href (they don't lose HTTP_REFERER like Internet Explorer 8 & lower does)
    else { 
        window.location.href = url; 
    }
}

function bloquearEnter() {
	 $('form input').on('keypress', function(e) {
		 return e.which !== 13;
	 });
	 
	 $('form select').on('keypress', function(e) {
		 return e.which !== 13;
	 });
}