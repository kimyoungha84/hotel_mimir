/**
 * 
 */
document.addEventListener('DOMContentLoaded', function () {
  const popupOverlay = document.querySelector('.popup-overlay04');
  const closeBtn = document.querySelector('.popup-close04');
  const checkBtn = document.querySelector('.checkBtn');

  closeBtn.addEventListener('click', function () {
    popupOverlay.style.display = 'none';
  });

  checkBtn.addEventListener('click', function () {
    popupOverlay.style.display = 'none';
  });
});