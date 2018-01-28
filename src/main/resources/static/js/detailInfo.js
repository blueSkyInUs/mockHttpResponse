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
                      var id=$(this).attr("data-id");
                      $.ajax({
                          type: 'delete',
                          url: '',
                          data: $("form").serialize(),
                          success: function(data) {
                          data=$.parseJSON(data);
                              if (data.status=='200'){
                              window.location.href="/admin/requestmeta/";
                              }else{
                              alert(data.errorMsg);
                              }
                          }
                      });

                });

    })