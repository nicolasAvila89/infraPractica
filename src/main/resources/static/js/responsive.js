var changed=false;
setInterval(function () {
    var h1 = jQuery("h1");
    if (changed){
        h1.addClass("greenFont");
    }else{
        h1.removeClass("greenFont");
    }
    changed=!changed;
},3000);