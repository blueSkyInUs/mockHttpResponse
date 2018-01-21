$(function(){

        $("button#doAdd").on("click",function(){
              var id=$(this).attr("data-id");
              $.ajax({
                  type: 'post',
                  url: '',
                  data: $("form").serialize(),
                  success: function(data) {
                      if (data=='success'){
                      window.location.reload();
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