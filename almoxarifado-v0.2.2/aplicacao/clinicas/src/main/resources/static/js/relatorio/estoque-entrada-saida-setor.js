function gerarPDF() {
	var dataMateriais = $("#table-material").DataTable().rows().data();
	var dataEntradas = $("#table-entradas").DataTable().rows().data();
	var dataSaidas = $("#table-saidas").DataTable().rows().data();
	
	moment.locale("pt-br");
	var __currentDate = moment().format('LLL');

	var __dataInicial = $("#inicio").val();
	var __dataFinal = $("#fim").val();
	var __nomeSetor = $("#nomeSetor").text();
	var __codigoSetor = $("#codigoSetor").text();

	var __materiais = [ [ {
		text : "Material",
		style : "tableHead"
	}, {
		text : "Estoque",
		style : "tableHead",
		alignment : "center"
	} ] ];

	if(dataMateriais.length === 0){
		__materiais.push([
			{
				text : 'Nenhum registro encontrado.',
				colSpan : 2,
				style : "tableCell",
				alignment : "center"
			}
		]);
	}else{
		$(dataMateriais).each(function(index, value) {
			__materiais.push([ {
				text : value[0],
				style : "tableCell",
				alignment : "justify"
			}, {
				text : value[1],
				style : "tableCell",
				alignment : "center"
			}]);
		});
	}
	
	var __entradas = [ [ {
		text : "Data",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Tipo",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Responsável",
		style : "tableHead",
		alignment : "left"
	} ] ];

	if(dataEntradas.length === 0){
		__entradas.push([
			{
				text : 'Nenhum registro encontrado.',
				colSpan : 3,
				style : "tableCell",
				alignment : "center"
			}
		]);
	}else{
		$(dataEntradas).each(function(index, value) {
			__entradas.push([ {
				text : $.parseHTML(value[0]),
				style : "tableCell",
				alignment : "center"
			}, {
				text : value[1],
				style : "tableCell",
				alignment : "center"
			}, {
				text : value[2],
				style : "tableCell",
				alignment : "justify"
			}]);
		});
	}
	
	var __saidas = [ [ {
		text : "Data",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Tipo",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Destino",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Responsável",
		style : "tableHead",
		alignment : "left"
	} ] ];

	if(dataSaidas.length === 0){
		__saidas.push([
			{
				text : 'Nenhum registro encontrado.',
				colSpan : 4,
				style : "tableCell",
				alignment : "center"
			}
		]);
	}else{
		$(dataSaidas).each(function(index, value) {
			__saidas.push([ {
				text : $.parseHTML(value[0]),
				style : "tableCell",
				alignment : "center"
			}, {
				text : value[1],
				style : "tableCell",
				alignment : "center"
			}, {
				text : value[2],
				style : "tableCell",
				alignment : "center"
			}, {
				text : value[3],
				style : "tableCell",
				alignment : "justify"
			}]);
		});
	}

	var document = {

		styles : {
			header : {
				fontSize: 12,
				bold: true, 
				alignment: 'center', 
				margin: [30, 25, 30, 0]
			},
			footer : {
				fontSize: 10,
				margin: [30, 0, 30, 25]
			},
			tableHead : {
	            fontSize: 11, 
	            bold: true
			},			
	        tableCell : {
	            fontSize: 10
	        },
	        infoData : {
	        	fontSize: 11,
	        	margin: [0, 10, 0, 0],
	        	bold : true
	        },
	        infoSetor : {
	        	fontSize: 11,
	        	margin: [0, 5, 0, 10],
	        	bold : true
	        },
	        title: {
	            fontSize: 14, 
	            bold: true, 
	            margin: [0, 10, 0, 10]
			},
		},

		pageSize : 'A4',
		
		header : function(currentPage, pageCount) {
			return {text : 'Relatório de Setor - Página ' + currentPage + ' de ' + pageCount, style : "header"}
		},

		content : [
			{
				columns : [
					{text : [
						'Data de início: ',
						{text: __dataInicial, bold: false}
					]},
					{text : [
						'Data de fim: ',
						{text: __dataFinal, bold: false}
					]}
				],
				style : "infoData"
			},
			{
				columns : [
					{
						text : [
							'Setor: ',
							{text: __nomeSetor, bold: false}
						]
					},
					{
						text : [
							'Código: ',
							{text: __codigoSetor, bold: false}
						]
					}
				],
				style : "infoSetor"
			},
			{
	            text: "Materiais", 
	            style: "title",
	            margin: [0, 0, 0, 10]
			},
			{
				table : {
					widths : [ "*", "auto"],
					headerRows : 1,
					body : __materiais
				},
				style : "content"
			},
			{
	            text: "Entradas", 
	            style: "title"
			},
			{
				table : {
					widths : [ "auto", "auto", "*" ],
					headerRows : 1,
					body : __entradas
				},
				style : "content"
			},
			{
	            text: "Saídas", 
	            style: "title"
			},
			{
				table : {
					widths : [ "auto", "auto", "auto", "*" ],
					headerRows : 1,
					body : __saidas
				},
				style : "content"
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
		style: "footer"
		}

	}

	pdfMake.createPdf(document).open();
}

$(document).ready(function() {

	// Select handle
	$("#setor").material_select();
	
	$("#form-busca").submit(function(event) {
		console.log("fdsf");
		
		
	});
	
	$(".download-pdf-button").click(function() {
		gerarPDF();
	});
	
	var oldSaidaFim = $(".val-saidas-end").val();
	var oldSaidaInicio = $(".val-saidas-begin").val();
	var oldEntradaInicio = $(".val-entradas-begin").val();
	var oldEntradaFim = $(".val-entradas-end").val();
	

});