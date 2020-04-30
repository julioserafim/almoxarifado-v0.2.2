$(document).ready(function() {
var showGeneratedPDF = function() {
		
		
		
		// playground requires you to assign document definition to a variable called dd

		var __currentDate = $(".val-data-atual").text();
		var __materiais = [
		        [
		            {text: "Material", style: "tableHead"},
		            {text: "Validade", style: "tableHead", alignment: "center"}, 
		            {text: "Lote", style: "tableHead", alignment: "center"},
		            {text: "Código Interno", style: "tableHead", alignment: "center"}
		        ]
		    ];
		    
		$(".val-material").each(function(_, el) {
			__materiais.push([
		        {text: $(el).find(".val-nome-material").text(), style: "tableCell", alignment: "justify"},
		        {text: $(el).find(".val-validade-material").text(), style: "tableCell", alignment: "center"}, 
		        {text: $(el).find(".val-lote-material").text(), style: "tableCell", alignment: "center"}, 
		        {text: $(el).find(".val-codigo-interno-material").text(), style: "tableCell", alignment: "center"},
		    ]);
		});

		var dd = {
		    
		    styles: {
				
				header: {
					fontSize: 12,
					bold: true, 
					alignment: 'center', 
					margin: [0, 20, 0, 0]
				},
				
				footer: {
					fontSize: 10,
					bold: true,  
					margin: [40, 0, 40, 0]
				},
				
				titleCenter: {
		            fontSize: 18, 
		            bold: true, 
		            alignment: "center", 
		            margin: [72, 72, 72, 72]
				}, 
				
				title: {
		            fontSize: 14, 
		            bold: true, 
		            margin: [0, 20, 0, 10]
				}, 
				
				attrTitle: {
		            bold: true, 
		            fontSize: 12
				}, 
				
				attrValue: {
		            bold: false, 
		            fontSize: 12
				}, 
				
				tableHead: {
		            fontSize: 12, 
		            bold: true
				}, 
				
		        tableCell: {
		            fontSize: 12, 
		            bold: false
				}
				
			}, 
		    
		    pageSize: 'A4',
		    
		    header: { 
		        text: '', 
		        style: "header"
		    },
		    
			content: [

				{
		            text: 'Relatório de Materiais Vencidos \nDia: ' + __currentDate, 
		            style: "titleCenter"
				},
				
				{
		            text: 'Materiais', 
		            style: "title"
				},
				
				{
		            table: {
		                widths: ["*", "auto", "auto", "auto",], 
		                headerRows: 1, 
		                body: __materiais
		            }
				}
				
			], 
			
			footer: {
		        columns: [
		            {   
		                text: 'Sistema de Gerenciamento de Estoque das Clínicas de Odontologia da UFC',
		                width: "auto"
		            }, 
		            {
		                text: __currentDate,
		                alignment: 'right', 
		                width: "*"
		            }
		        ], 
		        style: "footer"
			}
		    
		}
		
		pdfMake.createPdf(dd).open();
		
	}
	
	$(".download-pdf-button").click(function() {
		showGeneratedPDF();
	});
});