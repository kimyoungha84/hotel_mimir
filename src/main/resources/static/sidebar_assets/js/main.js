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

    /* COLLAPSE MENU */
    const linkCollapse = document.getElementsByClassName('collapse__link');
    

    var i;

    for(i=0;i<linkCollapse.length;i++) {
        linkCollapse[i].addEventListener('click', function(){
            /*모든 메뉴 접기*/
            const allMenus = document.querySelectorAll('.collapse__menu');
                allMenus.forEach(function(menu) {
                menu.classList.remove('showCollapse');
            });//allMenus

            const allArrows = document.querySelectorAll('.collapse__link .arrow-icon');
                allArrows.forEach(function(icon) {
                icon.classList.remove('rotate');
            });

            //감싼거 보여줘, 안보이게 해줘
            // const collapseMenu = this.nextElementSibling;
            // collapseMenu.classList.toggle('showCollapse');//showCollapse라는 속성을 추가/삭제 (on/off하는 것처럼 추가/삭제)
            


            // //화살표일거임.
            // const rotate = collapseMenu.previousElementSibling;
            // rotate.classList.toggle('rotate');

        /*클릭한 메뉴만 열기*/
        const collapseMenu = this.nextElementSibling;
        const arrowIcon = this.querySelector('.arrow-icon');

        const isOpen = collapseMenu.classList.contains('showCollapse');

        if (!isOpen) {
            collapseMenu.classList.add('showCollapse');
            if (arrowIcon) {
                arrowIcon.classList.add('rotate');
            }
        } else {
            collapseMenu.classList.remove('showCollapse');
            if (arrowIcon) {
                arrowIcon.classList.remove('rotate');
            }
        }
        });
    }//end for

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



