jQuery.noConflict();
jQuery(document).ready(function($) {
    
    //  INICIO EFEITOS MENU PRINCIPAL
    
    $(".menu-principal li a").click(function(){
        
        if( $(this).parent().find(".sub-menu").hasClass("menu-aberto")) {
            
            $(this).parent().animate({
                height: 42
            }, 450,function(){                    
                $(this).parent().find(".sub-menu").slideUp();
            });
            
            $(this).parent().find(".sub-menu").removeClass("menu-aberto");
            
        }
        else{
            
            if($(this).parent().hasClass("third-menu")) {
                
                $(this).parent().find(".sub-sub-menu").slideToggle();
                
                $(this).toggleClass("sub-menu-aberto")
                
            }else {
                
                $(this).parent().css("height","auto");
            
                $(this).parent().find(".sub-menu").slideDown();
                
                $(this).parent().find(".sub-menu").addClass("menu-aberto");
                
            } 
            
        }
        
    });
    
    //  FIM EFEITOS MENU PRINCIPAL
    
    // INICIO CORES TABELA
    
    //$(".tabela-dados table tr:even td").addClass("linha-cinza");
    
    $(".tabela-dados table tr").mouseover(function(){
        
        $(this).addClass("linha-over")
        
    }).mouseout(function(){
        
        $(this).removeClass("linha-over")
        
    })
    
    // FIM CORES TABELA
    
    // INICIO MASCARAS

    $(".maskData").mask("99/99/9999",{placeholder:" "});  // DATA
    $(".maskCpf").mask("999.999.999-99",{placeholder:" "});  // CPF
    $(".maskPasep").mask("999.99999.99/9",{placeholder:" "});  // PASEP
    $(".maskAgencia").mask("9999-9",{placeholder:" "});  // AGENCIA
    $(".maskConta").mask("9999999-9",{placeholder:" "});  // CONTA
    $(".maskCep").mask("99999-999",{placeholder:" "});  // CEP
    $(".maskTelefone").mask("(99)9999-9999",{placeholder:" "});  // TELEFONE
    $(".maskMatricula").mask("9999-9",{placeholder:" "});  // MATRICULA
    $(".maskMotorVeiculo").mask("9.9",{placeholder:" "});  // MOTOR VEICULO
    $(".maskPlacaVeiculo").mask("aaa-9999",{placeholder:" "});  // PLACA VEICULO
    $(".maskProcesso").mask("99999/9999-9",{placeholder:" "});  // NUMERO DO PROCESSO
    $(".maskQtdQuintos").mask("9",{placeholder:" "});  // QTD QUINTOS
    $(".maskSalario").maskMoney({precision:2});  // SALARIO
    $(".maskNoDocumento").mask("9999/9999",{placeholder:" "});  // NUMERO DO DOCUMENTO

    // FIM MASCARAS
    
});