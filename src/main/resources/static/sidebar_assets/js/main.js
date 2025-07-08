$(function(){

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

            $(".collapse__menu a").on("click", function(e){
                    e.stopPropagation();//이벤트 버블링 방지
            });

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
        });//click
    });//each

});//ready


/* ********************************************************************* */




