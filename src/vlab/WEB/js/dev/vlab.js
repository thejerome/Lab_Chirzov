var Vlab = {

    div : null,

    setVariant : function(str){},
    setPreviosSolution: function(str){},
    setMode: function(str){},

    //Инициализация ВЛ
    init : function(){
        this.div = document.getElementById("jsLab");
        this.div.innerHTML = this.window;
        document.getElementById("tool").innerHTML = this.tool;

        //получение варианта задания
        var ins = document.getElementById("preGeneratedCode").value;
    },

    getCondition: function(){},
    getResults: function(){},
    calculateHandler: function(text, code){},
}

window.onload = function() {
    Vlab.init();
};
