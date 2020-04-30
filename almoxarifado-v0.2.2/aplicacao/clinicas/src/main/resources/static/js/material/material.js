$(document).ready(function(){
	var url = _context + "/material/api/listar";
	$.ajax({
		dataType: "json",
		url: url,
		success: function(materiais) {
			
			function compare(a,b) {
				if (a.estoque < b.estoque)
					return -1;
				if (a.estoque > b.estoque)
					return 1;
				return 0;
			}

			materiais.sort(compare);
			
			var labels = materiais.map(function(m) { return m.nome; });
			var data = materiais.map(function(m) { return m.estoque; });
			var colors = mf_color.doGetRandomColor(materiais.length);
			var materialChart = new Chart($("#chart-material"),
				{
					type: 'bar', 
					data: {
						labels: labels,
						datasets: [{
				            label: 'Quantidade em Estoque',
				            data: data,
				            backgroundColor: colors,
				            borderColor: "#FFFFFF",
				            borderWidth: 1
				        }]
					}, 
					options: {
						legend: {
							display: false
						}, 
						scales:
				        {
				            xAxes: [{
				                display: false
				            }]
				        }
					}
				}
			);
			
			var dlMateriais = new DynamicList({
		    	list: ".dynamic-list", 
		    	element: ".dynamic-list-element",
		    	elementContainer: ".dynamic-list-container",
		    	addButton: ".dynamic-list-add",
		    	removeButton: ".dynamic-list-remove",
		    	insertAtBegin: true
		    });
			
			var i = 0;
			materiais.map(function(m) {
				var el = dlMateriais.addItem({ 
					".name": {
						"text": labels[i]
					}, 
					".estoque": {
						"text": data[i]
					}
				});
				el.find(".sample-circle").css("background-color", colors[i]);
				i++;
			});
			
		}
	});
	
	var dataTable = mf_base.doAddDataTable($("#table-material"));
	
	
});	