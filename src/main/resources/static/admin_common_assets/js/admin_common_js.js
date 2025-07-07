/**
 * 
 */
function loadCSS(href) {
    const existing = document.querySelector(`link[href="${href}"]`);
	alert("href======="+href);
    
    if (!existing) {
	    const link = document.createElement("link");
	    link.rel = "stylesheet";
	    link.href = href;
	    document.head.appendChild(link);
    }//end if

}//loadCSS