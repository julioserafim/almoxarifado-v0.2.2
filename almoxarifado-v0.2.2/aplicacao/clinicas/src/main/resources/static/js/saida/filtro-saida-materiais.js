 $(document).ready(function() {
	 
	 var status_andamento = 0;
	 var status_finalizada = 0;
	 var table = $('#table-listar-saidas').DataTable();
	 const listaCheckbox = ['#ultimos-30-dias', '#ultimos-3-meses', '#ultimo-semestre', '#ultimo-ano'];
	 const UM_ANO = 12;
     const SEMESTRE = 6;
     const DIAS_30 = 30;
     const MES_3 = 3;
     const MEDIA_DIAS_MES = 30.416666667;
     
     //Metodos utilitarios
     function mudarStatus(status) {
     	if(status == "EM ANDAMENTO") {
 			status_andamento++;
 		} else {
 			status_finalizada++;
 		}
     }
     
     function montarDate(date_str) {
         var date = date_str.split(/\/|-/);
         return new Date(date[2], date[1], date[0]);
     }
     
     function isDiferenca30Dias(date) {
         return dateFns.differenceInDays(new Date(), montarDate(date)) <= DIAS_30 ? true : false;
     }
     
     function isDiferenca3Meses(date) {
         if (dateFns.differenceInMonths(new Date(), montarDate(date)) <= MES_3 && dateFns.differenceInDays(new Date(), montarDate(date)) <= MES_3 * MEDIA_DIAS_MES) {
           return true;
         }
         return false;
     }
     
     function isDiferencaUmSemestre(date) {
         if (dateFns.differenceInMonths(new Date(), montarDate(date)) <= SEMESTRE && dateFns.differenceInDays(new Date(), montarDate(date)) <= SEMESTRE * MEDIA_DIAS_MES) {
           return true;
         }
         return false;
     }
     
     function isDiferencaUmAno(date) {
         if (dateFns.differenceInMonths(new Date(), montarDate(date)) <= UM_ANO && dateFns.differenceInDays(new Date(), montarDate(date)) <= UM_ANO * MEDIA_DIAS_MES) {
           return true;
         }
         return false;
      }
     
     function disabilitarTodosCheckboxExceto(id) {
     	for(var checkboxId of listaCheckbox) {
     		if (checkboxId != id) {
     			$(checkboxId).attr("disabled", "disabled");
     		}
     	}
     }
     
     function habilitarTodosCheckbox() {
     	for(var checkboxId of listaCheckbox) {
     		$(checkboxId).removeAttr("disabled");
     	}
     }
     
     // Adiciona um filtro ao datatable de acordo com os checkbox selecionados     
	 $.fn.dataTable.ext.search.push(
	    function( settings, data, dataIndex ) {
	        
	        var columnData = data[1].match(/\d{2}\/\d{2}\/\d*/g)[0];
	        var columnStatus = data[4];
	       
	        if($('#ultimos-30-dias').is(':checked')) {
	        	if(isDiferenca30Dias(columnData)) {
	        		mudarStatus(columnStatus);
	        		return true;
	        	}
	        	return false;
	        }
	        else if($('#ultimos-3-meses').is(':checked')) {
	        	if(isDiferenca3Meses(columnData)) {
	        		mudarStatus(columnStatus);
	        		return true
	        	}
	        	return false;
	        }
	        else if($('#ultimo-semestre').is(':checked')) {
	        	if(isDiferencaUmSemestre(columnData)) {
	        		mudarStatus(columnStatus);
	        		return true
	        	}
	        	return false;
	        }
	        else if($('#ultimo-ano').is(':checked')) {
	        	if(isDiferencaUmAno(columnData)) {
	        		mudarStatus(columnStatus);
	        		return true
	        	}
	        	return false;
	        }
	        
    		mudarStatus(columnStatus);

	        return true;
	    }
	);
	 
    
	// Adiciona eventos de click aos checkbox	 
	listaCheckbox.forEach(function(checkboxId) {
		$(checkboxId).on('click', function() {
			status_andamento = 0;
			status_finalizada = 0;
			
			if($(this).is(':checked')) {
		        disabilitarTodosCheckboxExceto(checkboxId);
			} else {
				habilitarTodosCheckbox();
			}
			
			table.draw();
			
			$("#statusAndamento").text(status_andamento);
			$("#statusFinalizada").text(status_finalizada);
	    } );
	});
	
} );