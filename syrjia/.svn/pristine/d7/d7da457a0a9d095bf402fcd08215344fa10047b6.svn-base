$(function() {
	$("body").on("click",".checkAll",function(){
		if(this.checked){
			$(this).parents("table").find("tbody input[type=checkbox]").prop("checked",true);
		}else{
			$(this).parents("table").find("tbody input[type=checkbox]").prop("checked",false);
		}
	});

	$(".am-tabs").on("blur","input[type=text]",function(event){
		//event.stopPropagation();
		$(this).css("border-style","hidden");
		$(this).removeClass("am-form-field");
		$(this).attr("readonly",true);
	});
	
	$(".am-tabs").on("focus","input[type=text]",function(event){
		//$(this).css("border-color","#fefffe");
	});
	
	$('.am-tabs').on('input oninput',"input[type=text]", function(event) {
		var size=Math.ceil(($(this).val().replace(/[^\u0000-\u00ff]/g,"aa").length)*1.2);
		var oldSize=$(this).attr("size");
		if(oldSize<size){
			$(this).attr("size",size);
		}
	}); 
	$(".am-tabs").on("dblclick","input[type=text]",function(event){
		//event.stopPropagation();
		//$(this).css("border-style","solid");
		//$(this).addClass("am-form-field");
		$(this).attr("readonly",false);
		$(this).val($(this).val());
	});
});
/*
// 禁用回退键
document.onkeydown = function (e) {
    var code;   
    if (!e){ var e = window.event;}   
    if (e.keyCode){ code = e.keyCode;}
    else if (e.which){ code = e.which;}
    // BackSpace 8;
    if (
      (event.keyCode == 8)
      && ((event.srcElement.type != "text" && event.srcElement.type != "textarea" &&  event.srcElement.type != "password")
        ||  event.srcElement.readOnly == true
        )
     
     ) {
        
     event.keyCode = 0;        
     event.returnValue = false;    
    }
    return true;
   };
*/
function fromValidator(id) {
	var $form = $('#' + id);
	var $tooltip = $('<div id="vld-tooltip">提示信息！</div>');
	$tooltip.appendTo(document.body);

	$form.validator();

	var validator = $form.data('amui.validator');

	$form.on('focusin focusout', '.am-form-error input', function(e) {
		if (e.type === 'focusin') {
			var $this = $(this);
			var offset = $this.offset();
			var msg = $this.data('foolishMsg')
					|| validator.getValidationMessage($this.data('validity'));

			$tooltip.text(msg).show().css({
				left : offset.left + 10,
				top : offset.top + $(this).outerHeight() + 10
			});
		} else {
			$tooltip.hide();
		}
	});
	$form.on('focusout', 'input', function(e) {
		$tooltip.hide();
	});
}

function validators(id) {
	var validator = $('#' + id).validator('isFormValid');
	if (!validator) {
		return false;
	}
	return true;
}


function beforeSend(){
	layer.load(2, {time: 100*1000}); // 又换了种风格，并且设定最长等待100秒
}
function complete(){
	layer.closeAll("loading"); // 又换了种风格，并且设定最长等待100秒
}

