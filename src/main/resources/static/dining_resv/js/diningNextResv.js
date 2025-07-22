/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
  let isPaymentSelected = false;
  let selectedPaymentText = "현장결제";
  const selectedPaymentInput = document.querySelector("#paymentType");

  const toggleBtn = document.getElementById("toggleRequestBtn");
  const requestContent = document.getElementById("requestContent");
  const checkAll = document.getElementById("checkAll");
  const otherChecks = document.querySelectorAll("input[name='check']");
  const requiredInputs = document.querySelectorAll(".input-field");
  const requiredChecks = document.querySelectorAll("input[name='check-required']");
  const payBtn = document.querySelector(".next-btn");
  const payBtnText = payBtn.querySelector("span");
  const onSiteBtn = document.querySelector(".field-btn");
  const onlineBtn = document.querySelector(".online-btn");
  const reselectPopup = document.getElementById("reselectPopup");
  const reselectBtn = document.getElementById("reselectBtn");
  const closePopupBtn = document.getElementById("closePopupBtn");

  // 토글 버튼
  toggleBtn.addEventListener("click", () => {
    const isVisible = requestContent.style.display === "block";
    requestContent.style.display = isVisible ? "none" : "block";
    toggleBtn.textContent = isVisible ? "＋" : "−";
    toggleBtn.setAttribute("aria-expanded", (!isVisible).toString());
  });

  // 입력 필드 유효성 검사
  document.querySelectorAll('.input-field').forEach(input => {
    const errorMessage = input.nextElementSibling;

    input.addEventListener('blur', () => {
      if (input.value.trim() === '') {
        input.classList.add('error');
        errorMessage?.classList.add('active');
      } else {
        input.classList.remove('error');
        errorMessage?.classList.remove('active');
      }
    });

    input.addEventListener('focus', () => {
      input.classList.remove('error');
      errorMessage?.classList.remove('active');
    });
  });

  // 전체 동의 체크박스
  checkAll.addEventListener("change", () => {
    const isChecked = checkAll.checked;
    document.querySelectorAll("input[type='checkbox']").forEach(chk => {
      chk.checked = isChecked;
    });
    validateAll();
  });

  otherChecks.forEach(chk => {
    chk.addEventListener("change", () => {
      const allChecked = Array.from(otherChecks).every(c => c.checked);
      checkAll.checked = allChecked;
      validateAll();
    });
  });

  // 예약 정보 변경 팝업
  document.querySelectorAll(".reservation-info01 button, .reservation-info02 button, .reservation-info03 button")
    .forEach(btn => {
      btn.addEventListener("click", () => {
        reselectPopup.style.display = "flex";
      });
    });

  closePopupBtn.addEventListener("click", () => {
    reselectPopup.style.display = "none";
  });

  reselectBtn.addEventListener("click", () => {
    window.location.href = "/diningResv";
  });

  // 결제 방법 버튼 클릭
  onSiteBtn.addEventListener("click", () => {
    isPaymentSelected = true;
    selectedPaymentText = "현장결제";
    selectedPaymentInput.value = selectedPaymentText;
    onSiteBtn.classList.add("selected");
    onlineBtn.classList.remove("selected");
    payBtnText.textContent = selectedPaymentText;
    validateAll();
  });

  onlineBtn.addEventListener("click", () => {
    isPaymentSelected = true;
    selectedPaymentText = "온라인결제";
    selectedPaymentInput.value = selectedPaymentText;
    onlineBtn.classList.add("selected");
    onSiteBtn.classList.remove("selected");
    payBtnText.textContent = selectedPaymentText;
    validateAll();
  });

  // 유효성 검사
  function validateAll() {
    const allFilled = Array.from(requiredInputs).every(input => input.value.trim() !== "");
    const allRequiredChecked = Array.from(requiredChecks).every(chk => chk.checked);

    if (allFilled && allRequiredChecked && isPaymentSelected) {
      payBtn.classList.remove("disabled");
      payBtn.setAttribute("aria-disabled", "false");
    } else {
      payBtn.classList.add("disabled");
      payBtn.setAttribute("aria-disabled", "true");
    }
  }

  requiredInputs.forEach(input => input.addEventListener("input", validateAll));
  requiredChecks.forEach(chk => chk.addEventListener("change", validateAll));

  // 결제 버튼 클릭 시 form 제출
  payBtn.addEventListener("click", (e) => {
    e.preventDefault();
    if (!payBtn.classList.contains("disabled")) {
      document.querySelector("#reservationForm").submit();
    }
  });
});