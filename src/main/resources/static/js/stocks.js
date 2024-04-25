
		$("#search").autocomplete({
		    source: function (request, response) {
		        $.getJSON("/search", { keywords: request.term }, function (data) {
		            response($.map(data.bestMatches, function (item) {
		                return { label: item.name + " (" + item.symbol + ")", value: item.symbol, name: item.name };
		            }));
		        });
		    },
		    select: function (event, ui) {
		        $("#search").val(ui.item.value);
		        $("#search").data("name", ui.item.name); 
		    }
		});
		
		function formatNumber(number) {
		    number = number.replace(/,/g, ''); 
		    return parseFloat(number).toLocaleString('en-US', {
		        minimumFractionDigits: 2,
		        maximumFractionDigits: 2
		    });
		}

var stockOHLCChart;
var stockVolumeChart;

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function initializeOHLCChart() {
    var ctx = document.getElementById('stockOHLCChart').getContext('2d');
    stockOHLCChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Open', 'High', 'Low', 'Close'],
            datasets: []
        },
        options: {
            scales: {
                x: { beginAtZero: true },
                y: { beginAtZero: true }
            }
        }
    });
}

function initializeVolumeChart() {
    var ctx = document.getElementById('stockVolumeChart').getContext('2d');
    stockVolumeChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Volume'],
            datasets: []
        },
        options: {
            scales: {
                x: { beginAtZero: true },
                y: { beginAtZero: true }
            }
        }
    });
}
function addStock() {
    $("#loadingBar").show();
    var symbol = $("#search").val(); // Retrieving the symbol from the search box
    var name = $("#search").data("name"); // Retrieving the name
    var randomColor = getRandomColor(); // Generate a random color
    if (symbol && name) {
        $.getJSON("/stock", { symbol: symbol }, function(stock) {
            if (stock && stock["Time Series (Daily)"]) {
                var stockData = stock["Time Series (Daily)"][Object.keys(stock["Time Series (Daily)"])[0]];
                if (stockData) {
                    var open = parseFloat(stockData["1. open"]);
                    var high = parseFloat(stockData["2. high"]);
                    var low = parseFloat(stockData["3. low"]);
                    var close = parseFloat(stockData["4. close"]);
                    var volume = parseFloat(stockData["5. volume"]);

                    $("#stocksTable").append("<tr><td>" + name + "</td><td>" + symbol + "</td><td>" + formatNumber(stockData["1. open"]) + "</td><td>" + formatNumber(stockData["2. high"]) + "</td><td>" + formatNumber(stockData["3. low"]) + "</td><td>" + formatNumber(stockData["4. close"]) + "</td><td>" + formatNumber(stockData["5. volume"]) + "</td></tr>");

				    // Update the OHLC chart with new data
				    stockOHLCChart.data.datasets.push({
				        label: name + ' (' + symbol + ')',
				        data: [open, high, low, close],
				        backgroundColor: randomColor + '66', 
               			borderColor: randomColor,
				        borderWidth: 1
				    });
				    stockOHLCChart.update();
				
				    // Update the Volume chart with new data
				    stockVolumeChart.data.datasets.push({
				        label: name + ' (' + symbol + ')',
				        data: [volume],
				        backgroundColor: randomColor + '66', 
                		borderColor: randomColor,
				        borderWidth: 1
				    });
				    stockVolumeChart.update();
                }
            } else {
                console.error("Unexpected structure in stock data:", stock);
            }
            $("#loadingBar").hide();
        });
    }
}

// Call this function when the document is ready to initialize the chart
$(document).ready(function() {
    initializeOHLCChart();
    initializeVolumeChart();
});

