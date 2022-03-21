<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
<title>Integrated Dictionary</title>
<style type="text/css">

body {
	background: #fff;
}

.col {
	display : inline-block;
	float: left;
	
}

input {
	width : 100%;
}

img {
	width : 200px;
}
.sels{
	background-color: rgb(241, 102, 103);
    color: rgb(255, 255, 255);
    word-break: break-all;
    cursor: pointer;
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-weight: bold;
    font-size: 12px;
    margin: 4px;
    padding: 3px 7px;
    display: inline-block;
    border-radius: 20px;
}
.listWrapper{
	display: flex;
    flex-wrap: wrap;
}
</style>
<link rel="favicon" href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/lib/bootstrap4/css/bootstrap.css"/>
<script src="${pageContext.request.contextPath}/resources/lib/bootstrap4/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/lib/jQuery3.6.0/jquery-3.6.0.min.js"></script>

<!-- <script src="resource/lib/qlickLogin/qlickLogin.js"></script> -->
</head>
<body>
<div class="container-fluid">

	<!--<img src="iisstart.png" alt="IIS" width="300" height="300" /> -->
	<div class="row">
		<div class="col-sm-2">
		</div>
		<div class="col-sm-8">
			<input id="keyWord" type="text" value="">
			<input type="button" value="search">
		</div>
		<div class="col-sm-2">
		</div>
	</div>
	<div class="row">
		<div class="col-12">
			<textarea id="fromDiv" autocomplete="on" style="width : 100%; height : 500px"></textarea>
			<input type="button" value="test" onclick="doIdic('fromDiv', 'targetSel')">
		</div>
	</div>
	<div class="row">
		<div class="listWrapper" id="targetSel">
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	
	console.log("${pageContext.request.contextPath}");
	
	$(document).ready(function() {
		
	});

	function doIdic(fromId, toId){
		let arr = [];
		let inHtml = "";
		const fId = fromId;
		const tId = toId;
		let tval = $("#" + fId).val();
		let vlen = tval.length;
		//console.log(vlen);
		let init = pushOneWord(vlen, tval);
		for(let i = 0; i < init.length; i++){
			if(arr.indexOf(init[i]) === -1 && init[i] !== ""){
				arr.push(init[i]);
			}
		}
		arr.sort();
		//console.log(arr);

		for (let i = 0; i < arr.length; i++){
			let r = Math.round(255*Math.random())
			let g = Math.round(255*Math.random())
			let b = Math.round(255*Math.random())
			if(r+g+b > 450){
				inHtml += "<div class='sels' style='background-color: rgb(" + r + "," + g + "," + b + "); color: rgb(42, 44, 52);'>" + arr[i] + "</div>";
			} else {
				inHtml += "<div class='sels' style='background-color: rgb(" + r + "," + g + "," + b + ")'>" + arr[i] + "</div>";
			}
		}
		//console.log(inHtml);
		$("#" + tId).html(inHtml);
	}
	
	function pushOneWord(startIdx, str){
		
		let rtnW = [];
		let tmp = [];
		let reg = /[^\w\sㄱ-힣]|[\_]/g;
		let reg2 = /\n/g;
		
		str.replace(reg2, " ");
		
		//console.log("startIdx ::: " + startIdx);
		//console.log("str ::: " + str);
		for (let i = startIdx - 2; i >= 0; i--){
			let txt = str[i];
			switch(str[i]){
				case "(":
					txt = " ";
					break;
				case ")":
					txt = " ";
					break;
				case "[":
					txt = " ";
					break;
				case "]":
					txt = " ";
					break;
				case "{":
					txt = " ";
					break;
				case "{":
					txt = " ";
					break;
				case "•":
					txt = " ";
					break;
				default:
					break;
			}
			
			if(i == 0){
				tmp.unshift(txt);
				commaSep(tmp, rtnW);
				return rtnW;
			}
			
			if(txt !== " "){
				tmp.unshift(txt);
			} else {
				commaSep(tmp, rtnW);
				tmp = [];
			}
		};
				
		function commaSep(arr, rtnArr){
			let t = (arr.join("")).replace(reg, "").replace(reg2, ".");
			let idx = t.indexOf(".");
			
			if(idx !== -1 && t.length !== 0){
				
				let fi = t.substr(0, idx);
				let li = (t.substr(idx + 1, t.length)).replace(/\./g, "");
				
				//console.log(t);
				//console.log("fi ::: " + fi);
				//console.log("li ::: " + li);
				
				if(fi.length !== 0){
					rtnArr.unshift(fi);
				}
				
				if(li.length !== 0){
					rtnArr.unshift(li);
				}
				
			} else {
				rtnArr.unshift(t);
			}
		};
		
		function classifiedConn(){

		}
	}
	
	
	
	

</script>
</html>