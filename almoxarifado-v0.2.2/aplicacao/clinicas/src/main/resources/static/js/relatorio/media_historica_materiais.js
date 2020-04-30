function mandarMensagem(elem, msg) {
	mf_base.doShowAlert(msg, 'warning', 0, 3000);
	elem.focus();
}

function gerarPDF() {
	var data = $("#tabelaMateriais").DataTable().rows().data();

	moment.locale("pt-br");
	var __currentDate = moment().format('LLL');


	var __setor = $("#destino option:selected").text();

	var __tipoBusca = $('input[name=tipo_busca]:checked', '#formulario').val();

	console.log(__tipoBusca);

	if (__tipoBusca == "busca_semestre") {
		var __dataInicial = $("#anoInicio").val();
		var __dataFinal = $("#anoFim").val();


		var __materiais = [[{
			text: "Material",
			style: "tableHead"
		}, {
			text: "Quantidade",
			style: "tableHead",
			alignment: "center"
		}, {
			text: "Qtd/Mês",
			style: "tableHead",
			alignment: "center"
		}, {
			text: "Qtd/Semestre",
			style: "tableHead",
			alignment: "center"
		},
		]];

		if (data.length === 0) {
			__materiais.push([
				{
					text: 'Nenhum registro encontrado.',
					colSpan: 2,
					style: "tableCell",
					alignment: "center"
				}
			]);

		} else {

			$(data).each(function (index, value) {
				__materiais.push([{
					text: value[0],
					style: "tableCell",
					alignment: "justify"
				}, {
					text: value[1],
					style: "tableCell",
					alignment: "center"
				}, {
					text: value[2],
					style: "tableCell",
					alignment: "center"
				}, {
					text: value[3],
					style: "tableCell",
					alignment: "center"
				},]);
			});

			var document = {

				styles: {
					header: {
						fontSize: 12,
						bold: true,
						alignment: 'center',
						margin: [30, 25, 30, 0]
					},
					footer: {
						fontSize: 10,
						margin: [30, 0, 30, 25]
					},
					tableHead: {
						fontSize: 11,
						bold: true
					},
					tableCell: {
						fontSize: 10
					},
					infoData: {
						fontSize: 11,
						margin: [0, 10, 0, 0],
						bold: true
					},
					infoSetor: {
						fontSize: 11,
						margin: [0, 5, 0, 10],
						bold: true
					}
				},

				pageSize: 'A4',

				header: function (currentPage, pageCount) {
					return { text: 'Relatório de Média Histórica de Materiais - Página ' + currentPage + ' de ' + pageCount, style: "header" }
				},

				content: [
					{
						columns: [
							{
								text: [
									'Ano de início: ',
									{ text: __dataInicial, bold: false }
								]
							},
							{
								text: [
									'Ano fim: ',
									{ text: __dataFinal, bold: false }
								]
							}
						],
						style: "infoData"
					},
					{
						text: [
							'Setor: ',
							{ text: __setor, bold: false }
						],
						style: "infoSetor"
					},
					{
						table: {
							widths: ['auto', 'auto', 'auto', 'auto'],
							headerRows: 1,
							body: __materiais
						},
						style: "content"
					}
				],

				footer: {
					columns: [
						{
							text: 'Sistema de Gerenciamento de Estoque das Clínicas de Odontologia da UFC',
							width: "auto"
						}, {
							text: __currentDate,
							alignment: 'right',
							width: "*"
						}],
					style: "footer"
				}

			}

			pdfMake.createPdf(document).open();
		}
	
	} else {
		var __dataInicial = $("#inicio").val();
		var __dataFinal = $("#fim").val();


		var __materiais = [[{
			text: "Material",
			style: "tableHead"
		}, {
			text: "Quantidade",
			style: "tableHead",
			alignment: "center"
		}, {
			text: "Média Mensal",
			style: "tableHead",
			alignment: "center"
		},
		]];


		if (data.length === 0) {
			__materiais.push([
				{
					text: 'Nenhum registro encontrado.',
					colSpan: 2,
					style: "tableCell",
					alignment: "center"
				}
			]);

			
		} else {

			$(data).each(function (index, value) {
				__materiais.push([{
					text: value[0],
					style: "tableCell",
					alignment: "justify"
				}, {
					text: value[1],
					style: "tableCell",
					alignment: "center"
				}, {
					text: value[2],
					style: "tableCell",
					alignment: "center"
				},]);
			});

			var document = {

				styles: {
					header: {
						fontSize: 12,
						bold: true,
						alignment: 'center',
						margin: [30, 25, 30, 0]
					},
					footer: {
						fontSize: 10,
						margin: [30, 0, 30, 25]
					},
					tableHead: {
						fontSize: 11,
						bold: true
					},
					tableCell: {
						fontSize: 10
					},
					infoData: {
						fontSize: 11,
						margin: [0, 10, 0, 0],
						bold: true
					},
					infoSetor: {
						fontSize: 11,
						margin: [0, 5, 0, 10],
						bold: true
					}
				},

				pageSize: 'A4',

				header: function (currentPage, pageCount) {
					return { text: 'Relatório de Média Histórica de Materiais - Página ' + currentPage + ' de ' + pageCount, style: "header" }
				},

				content: [
					{
						columns: [
							{
								text: [
									'Data de início: ',
									{ text: __dataInicial, bold: false }
								]
							},
							{
								text: [
									'Data de fim: ',
									{ text: __dataFinal, bold: false }
								]
							}
						],
						style: "infoData"
					},
					{
						text: [
							'Setor: ',
							{ text: __setor, bold: false }
						],
						style: "infoSetor"
					},
					{
						table: {
							widths: ['auto', 'auto', 'auto'],
							headerRows: 1,
							body: __materiais
						},
						style: "content"
					}
				],

				footer: {
					columns: [
						{
							text: 'Sistema de Gerenciamento de Estoque das Clínicas de Odontologia da UFC',
							width: "auto"
						}, {
							text: __currentDate,
							alignment: 'right',
							width: "*"
						}],
					style: "footer"
				}

			}

			pdfMake.createPdf(document).open();
		}

	}


		if (data.length === 0) {
			__materiais.push([
				{
					text: 'Nenhum registro encontrado.',
					colSpan: 2,
					style: "tableCell",
					alignment: "center"
				}
			]);
		} else {
			$(data).each(function (index, value) {
				__materiais.push([{
					text: value[0],
					style: "tableCell",
					alignment: "justify"
				}, {
					text: value[1],
					style: "tableCell",
					alignment: "center"
				}, {
					text: value[2],
					style: "tableCell",
					alignment: "center"
				},]);
			});
		}

		var document = {

			styles: {
				header: {
					fontSize: 12,
					bold: true,
					alignment: 'center',
					margin: [30, 25, 30, 0]
				},
				footer: {
					fontSize: 10,
					margin: [30, 0, 30, 25]
				},
				tableHead: {
					fontSize: 11,
					bold: true
				},
				tableCell: {
					fontSize: 10
				},
				infoData: {
					fontSize: 11,
					margin: [0, 10, 0, 0],
					bold: true
				},
				infoSetor: {
					fontSize: 11,
					margin: [0, 5, 0, 10],
					bold: true
				}
			},

			pageSize: 'A4',

			header: function (currentPage, pageCount) {
				return { text: 'Relatório de Média Histórica de Materiais - Página ' + currentPage + ' de ' + pageCount, style: "header" }
			},

			content: [
				{
					columns: [
						{
							text: [
								'Data de início: ',
								{ text: __dataInicial, bold: false }
							]
						},
						{
							text: [
								'Data de fim: ',
								{ text: __dataFinal, bold: false }
							]
						}
					],
					style: "infoData"
				},
				{
					text: [
						'Setor: ',
						{ text: __setor, bold: false }
					],
					style: "infoSetor"
				},
				{
					table: {
						widths: ['auto', 'auto', 'auto'],
						headerRows: 1,
						body: __materiais
					},
					style: "content"
				}
			],

			footer: {
				columns: [
					{
						text: 'Sistema de Gerenciamento de Estoque das Clínicas de Odontologia da UFC',
						width: "auto"
					}, {
						text: __currentDate,
						alignment: 'right',
						width: "*"
					}],
				style: "footer"
			}

		}

	}

	$(document).ready(function () {
		$("#tabelaMateriais").DataTable().order([[0, "asc"]]).draw();

		// Validar campos de data
		// $(".formRelSaida").submit(function(event) {
		// 	var dataInicio = moment($("#inicio").val(), "MM/YYYY").toDate();
		// 	var dataFim = moment($("#fim").val(), "MM/YYYY").toDate();

		//     console.log(moment($("#inicio").val()));

		// 	if (isNaN(dataInicio)) {
		// 		mandarMensagem($("#inicio"), "Data de início inválida");
		// 		event.preventDefault();
		// 	} else if (isNaN(dataFim)) {
		// 		mandarMensagem($("#fim"), "Data final inválida");
		// 		event.preventDefault();
		// 	} else if (dataInicio > dataFim) {
		// 		mandarMensagem($("#inicio"), "Data de início não pode ser maior que data fim");
		// 		event.preventDefault();
		// 	}

		// 	var dataInicio = $("#inicio").val();
		// 	var dataFim = $("#fim").val();

		// 	var pattern = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;

		// 	if (!pattern.test(dataInicio)) {
		// 		mandarMensagem($("#inicio"), "Data de início inválida");
		// 		event.preventDefault();
		// 	} else if (!pattern.test(dataFim)) {
		// 		mandarMensagem($("#fim"), "Data final inválida");
		// 		event.preventDefault();
		// 	}
		// });

		$(".download-pdf-button").click(function () {
			gerarPDF();
		});

		$('#formulario input[type=radio]').on('change', function () {
			var tipoBusca = $('input[name=tipo_busca]:checked', '#formulario').val();
			var buscaPeriodoDiv = $("#buscaPeriodo");
			var buscaSemestreDiv = $("#buscaSemestre");

			if (tipoBusca == "busca_semestre") {
				buscaSemestreDiv.show();
				buscaPeriodoDiv.hide();

				buscaSemestreDiv.children('input').each(function () {
					$(this).attr("required", "required");
				});

				buscaPeriodoDiv.children('input').each(function () {
					$(this).val("");
					$(this).removeAttr("required");
				});
			} else {
				buscaPeriodoDiv.show();
				buscaSemestreDiv.hide();

				buscaSemestreDiv.children('input').each(function () {
					$(this).val("");
					$(this).removeAttr("required");
				});

				buscaPeriodoDiv.children('input').each(function () {
					$(this).attr("required", "required");
				});
			}
		});

	});