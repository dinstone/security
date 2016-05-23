<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<style type="text/css">
.text-field {
	position: absolute;
	left: 40%;
}

label {
	display: inline-table;
	width: 90px;
	margin: 0px 0px 10px 20px;
}

img {
	width: 150px;
	margin: 0px 20px 10px 110px;
}

h2 {
	margin: 20px 20px 20px 40px;
}

button {
	margin: 20px 20px 10px 110px
}
</style>
<script type="text/javascript" src="jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#submit").click(function() {
			var req = {};
			req.username = $("#username").val();
			req.password = $("#password").val();

			var urls = "api/authen/login"
			$.ajax({
				type : "GET",
				url : urls,
				data : req,
				dataType : "json",
				success : function(msg) {
					$("#data").val(JSON.stringify(msg));
				},
				error : function(xhrequest, statusTxt, errorThrown) {
					var msg = "系统错误，请重试";
					if ("application/json;charset=UTF-8"==xhrequest.getResponseHeader("Content-Type")) {
						msg = JSON.parse(xhrequest.responseText);
						$("#data").val(msg.message);
					} else {
						$("#data").val(msg);
					}
				}
			});
		});
	});
</script>
</head>
<body>

	<div class="text-field">
		<h2>获取认证信息</h2>
		<form id="authen" method="post" onsubmit="return false;">
			<table>
				<tr>
					<td><label>username</label></td>
					<td><input type="text" name="username" id="username" /></td>
				</tr>
				<tr>
					<td><label>password</label></td>
					<td><input type="password" name="password" id="password" /></td>
				</tr>
				<tr>
					<td><button type="submit" id="submit">提交</button></td>
					<td><button type="reset">重置</button></td>
				</tr>
			</table>
		</form>
		<textarea rows="5" style="width: 100%" id="data"></textarea>
	</div>

</body>
</html>
