$(function () {

    const linkColor = document.querySelectorAll('.nav__link');

    // 기존 active 제거
    $('.collapse__sublink').removeClass('active');
    $('.nav__link.collapse').removeClass('active');
    $('.collapse__menu').removeClass('showCollapse');
    $('.collapse__link').removeClass('rotate');

    // 기본 nav 링크 클릭 시 active 처리
    linkColor.forEach(function (l) {
        l.addEventListener('click', function () {
            linkColor.forEach(el => el.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // collapse 클릭 이벤트 (하위 메뉴 열고 닫기)
    $(".nav__link.collapse").on("click", function (e) {
        const $clicked = $(this);
        const $menu = $clicked.children(".collapse__menu");
        const $arrow = $clicked.children(".collapse__link");

        // data-fixed-open이면 막음
        if ($clicked.attr('data-fixed-open') === 'true') {
            e.stopPropagation();
            return;
        }

        const isOpen = $menu.hasClass("showCollapse");

        // 모두 닫기
        $(".collapse__menu").removeClass("showCollapse");
        $(".collapse__link").removeClass("rotate");
        $(".nav__link.collapse").removeClass("active");

        if (!isOpen) {
            $menu.addClass("showCollapse");
            $arrow.addClass("rotate");
            $clicked.addClass("active");
        }

        e.stopPropagation();
    });

    // collapse 서브링크 클릭 시 localStorage 저장
    $('.collapse__sublink').on('click', function (e) {
        const href = $(this).attr('href');
        localStorage.setItem('activeSidebarPath', href);
    });

    // 페이지 로드시 메뉴 상태 복원
    const currentPath = window.location.pathname;
    const storedPath = localStorage.getItem('activeSidebarPath');

    $('.collapse__sublink').each(function () {
        const $sublink = $(this);
        const href = $sublink.attr('href');

        if (currentPath.includes(href) || (storedPath && storedPath.includes(href))) {
            const $collapseMenu = $sublink.closest('.collapse__menu');
            const $parentCollapse = $collapseMenu.closest('.nav__link');
            const $arrow = $parentCollapse.find('.collapse__link');

            $collapseMenu.addClass('showCollapse');
            $arrow.addClass('rotate');
            $sublink.addClass('active');
            $parentCollapse.addClass('active');
            $parentCollapse.attr('data-fixed-open', 'true');

            localStorage.removeItem('activeSidebarPath');
        }
    });

    // 기본 nav__link도 현재 주소 기준 active 처리
    $('.nav__link:not(.collapse)').each(function () {
        const href = $(this).attr('onclick')?.match(/'([^']+)'/)?.[1];
        if (href && currentPath.includes(href)) {
            $(this).addClass('active');
        }
    });

});
