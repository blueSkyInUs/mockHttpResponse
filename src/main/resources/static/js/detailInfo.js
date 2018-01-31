$(function(){
        $("input,textarea").on("change",function(){
             $("button#modify").removeAttr("disabled");
        });
        $("select").on("change",function(){
                     $("button#modify").removeAttr("disabled");
                });
        $("button#modify").on("click",function(){
              var url=$(this).attr("data-url");
              $.ajax({
                  type: 'put',
                  url: url,
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

        $("button#delete").on("click",function(){
                      var url=$(this).attr("data-url");
                      var home=$(this).attr("data-home");
                      $.ajax({
                          type: 'delete',
                          url: url,
                          data: $("form").serialize(),
                          success: function(data) {
                          data=$.parseJSON(data);
                              if (data.status=='200'){
                              window.location.href=home;
                              }else{
                              alert(data.errorMsg);
                              }
                          }
                      });

                });

    })