$(function(){
	$("#hideDefinitionSwitch").prop('checked', true);
	$("[id^=definition]").fadeTo(1,0.05);
	
	
	$("[id^=definition]").hover(
			function(){
				if($("#hideDefinitionSwitch").prop('checked')){
					$(this).fadeTo(100,1);
				}
			}, function(){
				if($("#hideDefinitionSwitch").prop('checked')){
					$(this).fadeTo(100,0.05);
				}
			});
	
	$("#hideDefinitionSwitch").change(function(){
		if($(this).prop('checked')){
			$("[id^=definition]").fadeTo(1,0.05);
		} else {
			$("[id^=definition]").fadeTo(1,1);
		}
	});
});	