/**
 * 
 */
document.addEventListener('DOMContentLoaded', function () {
	const popup = document.querySelector('.dining-popup');
	const overlay = document.querySelector('.popup-overlay01');
	const openBtn = document.getElementById('openDiningPopup');
	const closeBtn = document.querySelector('.popup-close01');

	openBtn.addEventListener('click', () => {
	  // 선택 초기화
	  document.querySelectorAll(".dining-popup .dining-item.selected").forEach(item => item.classList.remove("selected"));

	  // 서버에서 넘긴 ID에 따라 자동 선택
	  if (typeof selectDiningId !== 'undefined') {
	    const selectedItemElem = document.querySelector(`.dining-popup .dining-item[data-id='${selectDiningId}']`);
	    if (selectedItemElem) {
	      selectedItemElem.classList.add("selected");
	    }
	  }

	  popup.style.display = 'block';
	  overlay.style.display = 'block';

	  requestAnimationFrame(() => {
	    popup.classList.add('active');
	    overlay.classList.add('active');
	  });
	});

	closeBtn.addEventListener('click', () => {
	  popup.classList.remove('active');
	  overlay.classList.remove('active');

	  setTimeout(() => {
	    popup.style.display = 'none';
	    overlay.style.display = 'none';
	  }, 500);
	});
});