$(function(){
        $("input,textarea").on("change",function(){
             $("button#modify").removeAttr("disabled");
        });
        $("select").on("change",function(){
                     $("button#modify").removeAttr("disabled");
                });
        $("button#modify").on("click",function(){
              var id=$(this).attr("data-id");
              $.ajax({
                  type: 'put',
                  url: '',
                  data: $("form").serialize(),
                  success: function(data) {
                      if (data=='success'){
                      window.location.reload();
                      }
                  }
              });

        });
    })