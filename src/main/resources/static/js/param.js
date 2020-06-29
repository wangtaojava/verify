$(function() {
	$("#myform").find(":checkbox").click(function () {
        //点击取消
        if (this.checked === false) {
            $("#selectAll").prop('checked', false)
        }
        //点击选中
        else {
            var nocheckedList = $("#myform").find(":checkbox:not(:checked)").length;
            if (nocheckedList === 0) {
                $("#selectAll").prop('checked', true)
            }
        }
    });
    $("#selectAll").click(function() {
        $(":checkbox[name='param']").prop("checked", this.checked); // this指代的你当前选择的这个元素的JS对象
    });
});

function getParam(){
	
	var chk_value =[];//定义一个数组    
    $('input[name="param"]:checked').each(function(){
    	chk_value.push($(this).val());//将选中的值添加到数组chk_value中    
    });
    console.log(chk_value);
    if(!chk_value || chk_value.length==0){
    	alert("请选择功能");
    	return;
    }
    var email = $("#email").val();
    var mername = $("#mername").val();
    if(!mername){
    	alert("请填写商户名");
    	return;
    }else{
    	$.ajax({
		type: "post",
		url: "/VerifySystem/getParam",
		data: "chk="+chk_value+"&email="+email+"&mername="+mername,
		dataType: "json",
		success: function(data) {
			console.log(data);
			if(data){
				$("#channelid").text(data.channelid);
				$("#merid").text(data.merid);
				$("#termid").text(data.termid);
				$("#channelkey").text(data.channelkey);
//				$("#signkey").text(data.signkey);
			}else{
				alert("系统异常,稍后再试")
			}
			
		},
		error: function(err) {
			alert("访问失败,稍后再试")
		}
	});
    }
	
	
}