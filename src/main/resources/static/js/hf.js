		function validate() {
		    var pwd1 = $("#pwd1").val();
		    var pwd2 = $("#pwd2").val();
		    if(pwd1 == pwd2){
		    	remTelAtt();
		     }else {
				$("#tishi").html("两次密码不相同");
				$("#tishi").css("color","red");
				$("button").attr("disabled","disabled");  
				$("#pwd2").val("");  
				$("#pwd2").focus(); 
		      }
		}
		
		function verifyTel() {
		    var telphone = $("#telphone").val();
		    var regTelphone=/^[1][3,4,5,7,8][0-9]{9}$/;
		    if(regTelphone.test(telphone)){
		    	remTelAtt();
		     }else {
				$("#tishi_1").html("手机号码格式不正确");
		        $("#tishi_1").css("color","red");
		        $("button").attr("disabled","disabled");  
				$("#telphone").val("");  
				$("#telphone").focus();  
		      }
		}
		
		function remTelAtt() {
			$("button").removeAttr("disabled");
		}