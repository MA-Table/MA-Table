function pageLoad() {
	
	var user_id = encodeURI($("#qid").val());
	var user_pw = encodeURI($("#qpassword").val());
	var api_ = encodeURI("JTR6UJXZITJ0CIN");
	
	$.ajax({
        url: "https://statapi.nl.go.kr/api/login_ex.ajax?user_id=" + user_id + "&user_pw=" + user_pw + "&api=" + api_,
		type: "POST",
		dataType: "json",
		crossDomain: true,
        success : function(textStatus) {
        	console.log(textStatus.status);
        	console.log(typeof(textStatus.status));
        },
		
        error : function(textStatus) {
            // TODO : Error 처리
			console.log("error ::: ", textStatus);
		}
    });
	
	/*
	$.ajax({
        url : "https://statapi.nl.go.kr/api/login_ex.ajax?user_id=" + user_id + "&user_pw=" + user_pw + "&api=" + api_
        ,type : "GET"
        ,dataType: 'json'
        ,crossDomain: true
        ,success : function(data){
            console.log("==>"+data.status);
        }
        ,error:function(xhr){
            console.log(xhr);
        }
    });
	*/
}