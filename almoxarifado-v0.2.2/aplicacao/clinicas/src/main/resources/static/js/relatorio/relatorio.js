$(document).ready(function() {
	
	$(".material-card-color").each(function() {
		
		var estoque = parseInt($(this).find("span").text());
		
		if(estoque < 10) {
			$(this).addClass("red");
		}
		else if(estoque < 50) {
			$(this).addClass("orange");
		}
		else if(estoque < 100) {
			$(this).addClass("green");
		}
		else if(estoque < 200) {
			$(this).addClass("teal");
		}
		else if(estoque < 500) {
			$(this).addClass("cyan");
		}
		else if(estoque < 1000) {
			$(this).addClass("blue");
		}
		else {
			$(this).addClass("indigo");
		}
		
	});
		
});

$(document).ready(function () {
    var table = $('.datatable').DataTable({
    	destroy: true,
    	mark: true,
        dom: '<"top"B>t<"bottom"p><"center"i>',
        buttons: [
            {
                extend: 'excelHtml5',
                text:'<button class="download-excel-button btn-floating btn-large tooltipped" data-tooltip="Baixar Planilha"> <i class="mdi mdi-file-excel"></i> </button>',
                exportOptions: {
                    columns: ':not(.excel_ignore_coluna)'
                },
                extension: ".xls"
            }
        ],
        columDefs: [{
            targets: -1,
            visible: false
        }],
    	searching: false,
    	bPaginate: true,
    	pagingType: "full_numbers",
    	language: {
    		"sEmptyTable": "Nenhum registro encontrado",
            "sInfo": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
            "sInfoEmpty": "Mostrando 0 até 0 de 0 registros",
            "sInfoFiltered": "(Filtrados de _MAX_ registros)",
            "sInfoPostFix": "",
            "sInfoThousands": ".",
            "sLengthMenu": "_MENU_ resultados por página",
            "sLoadingRecords": "Carregando...",
            "sProcessing": "Processando...",
            "sZeroRecords": "Nenhum registro encontrado",
            "sSearch": "Filtrar",
	        "oPaginate": {
	            "sNext": "Próximo",
	            "sPrevious": "Anterior",
	            "sFirst": "Primeiro",
	            "sLast": "Último"
	        },
	        "oAria": {
	            "sSortAscending": ": Ordenar colunas de forma ascendente",
	            "sSortDescending": ": Ordenar colunas de forma descendente"
	        }
        }
    });

    $('.tooltipped').tooltip();
});
