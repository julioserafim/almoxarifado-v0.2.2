function gerarPDF() {
	var data = $("#tabelaMateriais").DataTable().rows().data();

	moment.locale("pt-br");
	var __currentDate = moment().format('LLL');

	var __dataValidade = $("#data").val();

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
		text : "Validade",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Lote",
		style : "tableHead",
		alignment : "center"
	}, {
		text : "Estoque",
		style : "tableHead",
		alignment : "center"
	} ] ];

	if(data.length === 0){
		__materiais.push([
			{
				text : 'Nenhum registro encontrado.',
				colSpan : 5,
				style : "tableCell",
				alignment : "center"
			}
		]);
	}else{
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
			},
			{
				text : value[2],
				style : "tableCell",
				alignment : "center"
			},
			{
				text : value[3],
				style : "tableCell",
				alignment : "center"
			}
			]);
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
	        info : {
	        	fontSize: 11,
	        	margin: [0, 5, 0, 10],
	        	bold: true
	        }
		},

		pageSize : 'A4',
		
		header : function(currentPage, pageCount) {
			return {text : 'Relatório de Vencimento de Materiais - Página ' + currentPage + ' de ' + pageCount, style : "header"}
		},

		content : [
			{
				text: [
					'Data de validade até: ',
					{text : __dataValidade, bold: false}
				],
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
					widths : [ "auto", "*", "auto", "auto", "auto" ],
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
		style: "footer"
		}

	}

	pdfMake.createPdf(document).open();
}

$(document).ready(function() {
	$('.datepicker').pickadate({
		min: new Date(moment()),
		format: 'dd/mm/yyyy',
        selectMonths: true, 
	    selectYears: 15,
	    onClose: function(){
	    	$("#formulario").submit();
	    }
    });	
	
//var showGeneratedPDF = function() {
//		
//		
//		
//		// playground requires you to assign document definition to a variable
//		// called dd
//
//		var __currentDate = mf_base.doGetCurrentDate();
//		var __validadeDate = $(".val-validade").val();
//		var __materiais = [
//		        [
//		            {text: "Material", style: "tableHead"},
//		            {text: "Unidade de medida", style: "tableHead", alignment: "center"},
//		            {text: "Validade", style: "tableHead", alignment: "center"}, 
//		            {text: "Lote", style: "tableHead", alignment: "center"}, 
//		            {text: "Estoque", style: "tableHead", alignment: "center"} 
//		        ]
//		    ];
//		    
//		$(".val-material").each(function(_, el) {
//			__materiais.push([
//		        {text: $(el).find(".val-nome-material").text(), style: "tableCell", alignment: "justify"},
//		        {text: $(el).find(".val-unidade-medida").text(), style: "tableCell", alignment: "center"}, 
//		        {text: $(el).find(".val-validade").text(), style: "tableCell", alignment: "center"}, 
//		        {text: $(el).find(".val-lote").text(), style: "tableCell", alignment: "center"}, 
//		        {text: $(el).find(".val-estoque").text(), style: "tableCell", alignment: "center"},
//		    ]);
//		});
//
//		var dd = {
//		    
//		    styles: {
//				
//				header: {
//					fontSize: 12,
//					bold: true, 
//					alignment: 'center', 
//					margin: [0, 20, 0, 0]
//				},
//				
//				footer: {
//					fontSize: 10,
//					bold: true,  
//					margin: [40, 0, 40, 0]
//				},
//				
//				titleCenter: {
//		            fontSize: 18, 
//		            bold: true, 
//		            alignment: "center", 
//		            margin: [72, 72, 72, 72]
//				}, 
//				
//				title: {
//		            fontSize: 14, 
//		            bold: true, 
//		            margin: [0, 20, 0, 10]
//				}, 
//				
//				attrTitle: {
//		            bold: true, 
//		            fontSize: 12
//				}, 
//				
//				attrValue: {
//		            bold: false, 
//		            fontSize: 12
//				}, 
//				
//				tableHead: {
//		            fontSize: 12, 
//		            bold: true
//				}, 
//				
//		        tableCell: {
//		            fontSize: 12, 
//		            bold: false
//				}
//				
//			}, 
//		    
//		    pageSize: 'A4',
//		    
//		    header: { 
//		        text: '', 
//		        style: "header"
//		    },
//		    
//			content: [
//
//				{
//		            text: 'Relatório de Materiais Com Validade Até\n' + __validadeDate, 
//		            style: "titleCenter"
//				},
//				
//				{
//		            text: 'Materiais', 
//		            style: "title"
//				},
//				
//				{
//		            table: {
//		                widths: ["*", "auto", "auto", "auto", "auto"], 
//		                headerRows: 1, 
//		                body: __materiais
//		            }
//				}
//				
//			], 
//			
//			footer: {
//		        columns: [
//		            {   
//		                text: 'Sistema de Gerenciamento de Estoque das Clínicas de Odontologia da UFC',
//		                width: "auto"
//		            }, 
//		            {
//		                text: __currentDate,
//		                alignment: 'right', 
//		                width: "*"
//		            }
//		        ], 
//		        style: "footer"
//			}
//		    
//		}
//		
//		pdfMake.createPdf(dd).open();
//		
//	}
	
	$(".download-pdf-button").click(function() {
		gerarPDF();
	});
});