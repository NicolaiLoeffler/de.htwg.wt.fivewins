$(function() {

	connect();

	function connect() {
		var socket = new WebSocket("ws://localhost:9000/socket");

		message('Socket Status: ' + socket.readyState + ' (ready)');

		socket.onopen = function() {
			message('Socket Status: ' + socket.readyState + ' (open)');
		};

		socket.onmessage = function(msg) {
			var data = msg.data;
			updateField(data);
		};

		socket.onclose = function() {
			message('Socket Status: ' + socket.readyState + ' (Closed)');
		};

		function send() {
			var grid = "";
			socket.send(grid);
			message('Sent grid ');
		}

	}
	// End connect

});

function pressed(x, y) {

	// send ajax call
	$.post("/setCell/" + x + "/" + y, function(data) {
		var msg = JSON.parse(data);

		$('#testOutPut').append("Is draw:" + msg.isDraw);
		$('#testOutPut').append("Player sign:" + msg.playerSign);
		$('#testOutPut').append("Status: " + msg.status);
		$('#testOutPut').append("Is won: " + msg.isWon);
		$('#testOutPut').append("Winner:" + msg.winner);
		$('#testOutPut').append("<br>");
		$('#testOutPut').append(msg.field);

		// Cell already set
		if (msg.status.indexOf("already") > -1) {
			alert("Cell is already set!");
			return

		}

		// Cell successfully set -> update div cell
		if (msg.status.indexOf("successfully") != -1) {

			if (msg.playerSign == "O") {
				$("#" + x + y).css('color', 'red');
			} else {
				$("#" + x + y).css('color', 'blue');
			}
			$("#" + x + y).append(msg.playerSign);

		}

		if (msg.isWon == "true") {
			alert("Player " + msg.winner + " won!");
			$
			return;
		}
		if (msg.isDraw == "true") {
			alert("Game resulted in a draw!");
			return;
		}
	})
}

function updateField(field) {
	var field = field.replace(/\s/g, '');
	var columns = Math.sqrt(field.length);
	var rows = columns;
	alert("update called");
	for (i = 0; i < rows; i++) {
		for (j = 0; j < columns; j++) {
			if (field.charAt(i + j) != "-") {
				alert(i + " " + j + " appended");
				$('#' + i + j).append(field.charAt(i + j));
			}
		}
	}
}