/**
 * 
 */
document.addEventListener('DOMContentLoaded', function () {
	const popup = document.querySelector('.dining-popup');
	const overlay = document.querySelector('.popup-overlay01');
	const openBtn = document.getElementById('openDiningPopup');
	const closeBtn = document.querySelector('.popup-close01');

	openBtn.addEventListener('click', () => {
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