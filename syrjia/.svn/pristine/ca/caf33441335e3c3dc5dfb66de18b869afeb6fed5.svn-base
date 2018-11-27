// 绑定jquery tree选择和取消选择事件
function treeHandle() {
	// 选择事件
	$('#firstTree').on('selected.tree.amui', function(event, data) {
		try {
			var type = data.target.type;
			if (type == "item") {
				var permissions = $("#" + data.target.attr.id);
				getParent(permissions)
			}
		} catch (e) {
			return true;
		}
	});
	// 取消选择事件
	$('#firstTree').on('deselected.tree.amui', function(event, data) {
		try {
			var type = data.target.type;
			if (type == "item") {
				var permissions = $("#" + data.target.attr.id);
				removeParentClass(permissions);
			}
		} catch (e) {
			return true;
		}
	});
}

function getParent(obj) {
	var pid = $(obj).attr("pid");
	if (null != pid && pid != 0) {
		var permissions = $("#permissios_" + pid);
		permissions.addClass("am-tree-selected");
		getParent(permissions);
	}
}

function removeParentClass(obj) {
	var pid = $(obj).attr("pid");
	if (null != pid && pid != 0) {
		var flag = true;
		if ($(obj).siblings().length > 0) {
			$(obj).siblings().each(function(index, item) {
				if ($(this).hasClass('am-tree-selected')) {
					flag = false;
				}
			});
		}
		if (flag) {
			var permissions = $(obj).parent().parent();
			permissions.removeClass("am-tree-selected");
			removeParentClass(permissions);
		}
	}
}
