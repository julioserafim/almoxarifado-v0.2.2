function gerarPDF() {
	var data = $("#tabelaMateriais").DataTable().rows().data();

	moment.locale("pt-br");
	var __currentDate = moment().format('LLL');

	var __dataReferencia = $("#dataReferencia").text();
	
	var __vencidosInclusos;
	if ($("#incluir_vencidos").is(":checked")) {
		__vencidosInclusos = "SIM";
	} else {
		__vencidosInclusos = "NÃO";
	}

	var __materiais = [ [
	{
		text : "#",
		style : "tableHead",
		alignment : "center"
	},
	{
		text : "Material",
		style : "tableHead"
	}, {
		text : "Estoque",
		style : "tableHead",
		alignment : "center"
	}, ] ];

	if (data.length === 0) {
		__materiais.push([ {
			text : 'Nenhum registro encontrado.',
			colSpan : 2,
			style : "tableCell",
			alignment : "center"
		} ]);
	} else {
		$(data).each(function(index, value) {
			__materiais.push([
			{
				text : index + 1,
				style : "tableCell",
				alignment : "center"
			},
			{
				text : value[0],
				style : "tableCell",
				alignment : "justify"
			}, {
				text : value[1],
				style : "tableCell",
				alignment : "center"
			}, ]);
		});
	}

	var document = {

		styles : {
			header : {
				fontSize : 12,
				bold : true,
				alignment : 'center',
				margin : [ 30, 25, 30, 0 ]
			},
			footer : {
				fontSize : 10,
				margin : [ 30, 0, 30, 25 ]
			},
			tableHead : {
				fontSize : 11,
				bold : true
			},
			tableCell : {
				fontSize : 10
			},
			info : {
				fontSize : 11,
				margin : [ 0, 5, 0, 10 ],
				bold : true
			}
		},

		pageSize : 'A4',

		header : function(currentPage, pageCount) {
			return {
				text : 'Relatório de Materiais em Falta - Página ' + currentPage + ' de ' + pageCount,
				style : "header"
			}
		},

		content : [
		{
			columns : [{
				text : [ 'Materiais vencidos inclusos: ', {
					text : __vencidosInclusos,
					bold : false
				} ]
			} ],
			style : "info"
		},
		{
			text: [
				'TOTAL DE ITENS: ',
				{text : __materiais.length - 1, bold : false}
			],
			style : "info"
		},
		{
			table : {
				widths : [ "auto", "*", "auto"],
				headerRows : 1,
				body : __materiais
			},
			style : "content"
		},
		{
			text: [
				'TOTAL DE ITENS: ',
				{text : __materiais.length - 1, bold : false}
			],
			style : "info"
		}
		],

		footer : {
			columns : [
					{
						text : 'Sistema de Gerenciamento de Estoque das Clínicas de Odontologia da UFC',
						width : "auto"
					}, {
						text : __currentDate,
						alignment : 'right',
						width : "*"
					} ],
			style : "footer"
		}

	}

	pdfMake.createPdf(document).open();
}

$(document).ready(function() {

	$("input[id='incluir_vencidos']").change(function() {
		if ($(this).is(":checked")) {
			window.location.replace(_context + "/relatorio/materiais_sem_estoque/incluir_vencidos=true");
		} else {
			window.location.replace(_context+ "/relatorio/materiais_sem_estoque/incluir_vencidos=false");
		}
	});

					

	$(".download-pdf-button").click(function() {
		gerarPDF();
	});
});