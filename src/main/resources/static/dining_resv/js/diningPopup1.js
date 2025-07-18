/**
 * 
 */
document.addEventListener('DOMContentLoaded', function () {
  document.querySelector('.popup-close01').addEventListener('click', function () {
    document.querySelector('.dining-popup').style.display = 'none';
    document.querySelector('.popup-overlay01').style.display = 'none';
  });
});