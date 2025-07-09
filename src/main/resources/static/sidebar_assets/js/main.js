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


    const currentPath = window.location.pathname;



    $('.collapse__sublink').each(function () {
        const $sublink = $(this);
        const href = $sublink.attr('href');

        if (currentPath.includes(href)) {
        const $collapseMenu = $sublink.closest('.collapse__menu');
        const $parentCollapse = $collapseMenu.closest('.nav__link');
        const $arrow = $parentCollapse.find('.collapse__link');

        // 펼친 상태 유지
        $collapseMenu.addClass('showCollapse');
        $arrow.addClass('rotate');
        
        // 클릭된 메뉴 강조
        $sublink.addClass('active');
        $parentCollapse.addClass('active');

        // "유지 고정" 표시
        $parentCollapse.attr('data-fixed-open', 'true');
        }
    });//each

    // 일반 nav__link (예: 대쉬보드, 직원관리 등)
    $('.nav__link:not(.collapse)').each(function () {
        const href = $(this).attr('onclick')?.match(/'([^']+)'/)?.[1];

        if (href && currentPath.includes(href)) {
        $(this).addClass('active');
        }
    });

  // 토글 클릭 이벤트
    $(".nav__link.collapse").each(function () {
        $(this).on("click", function (e) {
        // 자동으로 열린 메뉴는 닫히지 않게
        if ($(this).attr('data-fixed-open') === 'true') {
            e.stopPropagation();
            return;
        }

        // 나머지 메뉴 닫기
        $(".collapse__menu").removeClass("showCollapse");
        $(".collapse__link").removeClass("rotate");
        $(".nav__link.collapse").removeClass("active");

        // 현재 클릭한 메뉴 열기
        $(this).children(".collapse__menu").addClass("showCollapse");
        $(this).children(".collapse__link").addClass("rotate");
        $(this).addClass("active");

        e.stopPropagation();
        });//on
    });//each
});//ready


/* ********************************************************************* */
