<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="label" type="java.lang.String" required="false"%>
<button class="btn btn-white btn-sm" onclick="returnAll()" data-toggle="tooltip" data-placement="top"><i class="fa fa-trash-o"> ${label==null?'退货':label}</i>
                        </button>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入table的id和controller的url --%>
<script type="text/javascript">
$(document).ready(function() {
    $('#${id} thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	  $('#${id} tbody tr td input.i-checks').iCheck('check');
    	});

    $('#${id} thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	  $('#${id} tbody tr td input.i-checks').iCheck('uncheck');
    	});
    
});
	
function returnAll(){
	var selectedIps = [];
	var selectedFnums = [];
	$(":checkbox:checked").closest("tr").find(":text").each(function(i, eleDom){
		// 遍历每个被选中的复选框所在行的文本框的值, 看你怎么用了，你要哪个
		//alert('第 ' + (i+1) + ' 个文本框的值: ' + eleDom.value);
		if(i%2==1){
		selectedFnums.push(eleDom.value);
		}
	});
	$(":checkbox:checked").each(function(i, eleDom){
		// 遍历每个被选中的复选框所在行的文本框的值, 看你怎么用了，你要哪个
		//alert('第 ' + (i+1) + ' 个文本框的值: ' + eleDom.value);
		//alert(eleDom.value);
		//if(i==0){
		selectedIps.push(eleDom.value);
		//}
	});
	
	if(selectedIps.length>0){
		
		var ids = "";
		for(var i=0;i<selectedIps.length;i++){
			ids += selectedIps[i]+":"+selectedFnums[i]+";";
		}
		ids = ids.substring(0,ids.length-1);
		top.layer.confirm('确定要退货吗?', {icon: 3, title:'系统提示'}, function(index){
			window.location = "${ctx}/order/procurement/orderProDetail/returnAll?ids="+ids;
			top.layer.close(index);
		});
		
	}else{
		top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
		return;
	}
}
</script>