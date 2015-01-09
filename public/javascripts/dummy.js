$(document).ready(function () {
    //gametype buttons
    $('.btn.btn-default.btn-md').click(function(e) {
        e.preventDefault();
        $('#pop-up-1').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
    
    $('.btn.btn-default.btn-md.active').click(function(e) {
        e.preventDefault();
        $('#pop-up-1').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
    
    //range slider
    $('#range').click(function(e) {
        e.preventDefault();
        $('#pop-up-2').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
    
    //play now button
    $('#play_now').click(function(e) {
        e.preventDefault();
        $('#pop-up-3').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
    
    //preview
    $('#preview').click(function(e) {
        e.preventDefault();
        $('#pop-up-4').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
    
    //game cell
    $("#dummy_play_field").click(function(e) {
        e.preventDefault();
        $('#pop-up-5').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
    
    //further game information
    $('#gameOptions').click(function(e) {
        e.preventDefault();
        $('#pop-up-6').popUpWindow({
        		action: "open",
            	size: "small"
            });
    });
});

