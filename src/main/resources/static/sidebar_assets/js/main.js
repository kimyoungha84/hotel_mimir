$(function(){

    //showMenu('nav-toggle', 'navbar', 'body-pd');

    /* LINK ACTIVE */
    const linkColor = document.querySelectorAll('.nav__link');


    /*colorLink*/
    function colorLink() {
        linkColor.forEach(function(l){
            l.classList.remove('active')
        });
        this.classList.add('active');
    }//colorLink


    linkColor.forEach(function(l){ 
        l.addEventListener('click', colorLink)
    });

    var collapseLinks = $(".nav__link.collapse");

    collapseLinks.each(function () {
        $(this).on("click", function (e) {
			
            var clicked = $(this);
            var clickedMenu = clicked.children(".collapse__menu");
            var clickedArrow = clicked.children(".collapse__link");

            // 이미 열려있는 상태면 닫기만 하고 return
            if (clickedMenu.hasClass("showCollapse")) {
                clickedMenu.removeClass("showCollapse");
                clickedArrow.removeClass("rotate");
                return;
            }

            // 모든 메뉴 닫기
            $(".collapse__menu").removeClass("showCollapse");
            $(".collapse__link").removeClass("rotate");

            // 현재 클릭한 메뉴 열기
            clickedMenu.addClass("showCollapse");
            clickedArrow.addClass("rotate");

            e.stopPropagation();
        });
    });
});//ready


/* ********************************************************************* */

/* EXPANDER MENU */
/*showMenu*/
function showMenu(toggleId, navbarId, bodyId) {

    const toggle = document.getElementById(toggleId),
    navbar = document.getElementById(navbarId),
    bodypadding = document.getElementById(bodyId);
    
    /*버튼을 클릭했을 때 toggle*/
    if( toggle && navbar ) {
        toggle.addEventListener('click', function(){
            const isNowExpanded = navbar.classList.toggle("expander");
            bodypadding.classList.toggle("shrink");



        });
    }//end if
}//function showMenu



