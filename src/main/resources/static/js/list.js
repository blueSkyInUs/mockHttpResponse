$(function(){

        $("button#doAdd").on("click",function(){
              var id=$(this).attr("data-id");
              $.ajax({
                  type: 'post',
                  url: '',
                  data: $("form").serialize(),
                  success: function(data) {
                      data=$.parseJSON(data);
                      if (data.status=='200'){
                         window.location.reload();
                      }else{
                         alert(data.errorMsg);
                      }
                  }
              });

        });
        $("button#close").on("click",function(){
          $('input').val('');
        });
        $("#closeLeftUpper").on("click",function(){
          $("button#close").click();
        });
    })