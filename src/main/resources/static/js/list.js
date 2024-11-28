$(function (){
    //페이징 헤더
    $("[name = 'pageRows']").change(function (){
        $("[name='frmPageRows']").attr({
            'method': 'Post',
            'action': 'pageRows',
        }).submit()
    })
})