/**
 * 
 */
document.addEventListener("DOMContentLoaded", function () {
  const maxCount = 10;
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

  // 선택 버튼 클릭 시 인원수 반영
  const confirmBtn = document.querySelector(".cntBtn");
  confirmBtn.addEventListener("click", function () {
    const adultCnt = document.getElementById("adultCnt").textContent.trim();
    const childCnt = document.getElementById("childCnt").textContent.trim();

    // visible span 반영
    const adultTxtSpan = document.querySelector(".cnt-adult-txt");
    const childTxtSpan = document.querySelector(".cnt-child-txt");
    if (adultTxtSpan) adultTxtSpan.textContent = adultCnt;
    if (childTxtSpan) childTxtSpan.textContent = childCnt;

    // hidden input 반영 (폼 제출용)
    const inputAdult = document.getElementById("inputAdult");
    const inputChild = document.getElementById("inputChild");
    if (inputAdult) inputAdult.setAttribute("value", adultCnt);
    if (inputChild) inputChild.setAttribute("value", childCnt);

    // 팝업 닫기
    popup.classList.remove("active");
    overlay.classList.remove("active");
    setTimeout(() => {
      popup.style.display = "none";
      overlay.style.display = "none";
    }, 500);
  });

  const popup = document.querySelector(".cnt-popup");
  const overlay = document.querySelector(".popup-overlay02");
  const closeBtn = document.querySelector(".popup-close02");
  const openBtn = document.getElementById("openCntPopup");

  window.openCntPopup = function () {
    const currentAdult = document.querySelector(".cnt-adult-txt")?.textContent.trim() || "1";
    const currentChild = document.querySelector(".cnt-child-txt")?.textContent.trim() || "0";

    document.getElementById("adultCnt").textContent = currentAdult;
    document.getElementById("childCnt").textContent = currentChild;

    popup.style.display = "block";
    overlay.style.display = "block";

    requestAnimationFrame(() => {
      popup.classList.add("active");
      overlay.classList.add("active");
    });
  };

  closeBtn.addEventListener("click", function () {
    popup.classList.remove("active");
    overlay.classList.remove("active");

    setTimeout(() => {
      popup.style.display = "none";
      overlay.style.display = "none";
    }, 500);
  });

  if (openBtn) {
    openBtn.addEventListener("click", openCntPopup);
  }
});