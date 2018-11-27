function Choose(id, val) {
	this.id = id;
	this.val = val;
}
var choice = new Array();

function addLocalStore(qid, val) {
	var choose = new Choose(qid, val);
	if (null == choice) {
		choice = new Array();
	}
	$.each(choice, function(index, item) {
		if (item && item.id == qid) {
			choice.splice(index, 1);
		}
	});
	choice.push(choose);
}

var url = window.location.href;
var isRefurbish = 1;
var store = $.AMUI.store;
var historyChioce = store.get("server_goods");
choice = historyChioce;
if (choice == null) {
	choice = new Array();
}
jQuery(document).ready(function($) {
	if (window.history && window.history.pushState) {
		$(window).on('popstate', function() {
			var hashLocation = location.hash;
			var hashSplit = hashLocation.split("#!/");
			var hashName = hashSplit[1];
			if (hashName !== '') {
				var hash = window.location.hash;
				if (hash === '') {
					isRefurbish = 2;
					if (choice && choice.length > 0) {
						store.set("server_goods", choice);
					} else {
						window.history.go(-1);
					}
				}
			}
		});
		window.history.pushState('forward', null, url);
	}
});
$(window).unload(function(evt) {
	if (isRefurbish == 1) {
		store.set("server_goods", choice);
	}
});
function showLocalData(id) {
	var map="";
	$.each(choice,
			function(index, item) {
				if(item&&item.id==id){
					map=item.val;
				}
			});
	return map;
}