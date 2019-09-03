setInterval(function () {
    var elementById = document.getElementById("hello");
    if (elementById.changed == undefined){
        elementById.changed=false;
    }else{
        elementById.changed=!elementById.changed;
    }
    if (elementById.changed){
        elementById.classList.add("greenFont");
    }else{
        elementById.classList.remove("greenFont");
    }
},3000);