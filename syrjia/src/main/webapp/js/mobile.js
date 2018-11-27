// $(document).ready(function(){
$(window).on("load",function(){
    var $windowWidth = $(window).width();
    var $designWidth = $("body").attr("data-design-width");
    var $designdpr = $("body").attr("data-design-dpr");
    //$("html").attr("data-dpr",window.devicePixelRatio);
    if(!$designWidth){
        $designWidth = 640;
    }
    if(!$designdpr){
        $designdpr = 2;
    }
    // console.log($windowWidth,$designWidth,$designdpr);
    setTimeout(function(){
        $windowWidth = $(window).width();
        if($windowWidth > $designWidth){
            $windowWidth = $designWidth;
        }
        $("html").css("font-size",(100/($designWidth/$designdpr)) * $windowWidth + "px");
    },100);


    $(window).resize(function(){
        $windowWidth = $(window).width();
        if($windowWidth > $designWidth){
            $windowWidth = $designWidth;
        }
        $("html").css("font-size",(100/($designWidth/$designdpr)) * $windowWidth + "px");
    });
})

// });
