/**
 * 
 */
document.addEventListener("DOMContentLoaded", function () {
  const maxCount = 20;
  const minCount = 0;

  document.querySelectorAll(".plus, .minus").forEach(button => {
    button.addEventListener("click", function () {
      const type = this.dataset.type;
      const countSpan = document.getElementById(type + "Cnt");
      let count = parseInt(countSpan.textContent);

      if (this.classList.contains("plus") && count < maxCount) {
        count++;
      } else if (this.classList.contains("minus") && count > minCount) {
        count--;
      }

      countSpan.textContent = count;
    });
  });

  const closeBtn = document.querySelector(".popup-close02");
  const popup = document.querySelector(".cnt-popup");
  const overlay = document.querySelector(".popup-overlay02");

  closeBtn.addEventListener("click", function () {
    popup.style.display = "none";
    overlay.style.display = "none";
  });
});