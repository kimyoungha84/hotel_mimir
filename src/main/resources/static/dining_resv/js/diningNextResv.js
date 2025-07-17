/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.getElementById("toggleRequestBtn");
  const requestContent = document.getElementById("requestContent");

  toggleBtn.addEventListener("click", () => {
    const isVisible = requestContent.style.display === "block";
    requestContent.style.display = isVisible ? "none" : "block";
    toggleBtn.textContent = isVisible ? "＋" : "−";
    toggleBtn.setAttribute("aria-expanded", (!isVisible).toString());
  });

  // 입력필드 유효성 체크
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
  
  // 전체 동의 체크박스 로직
  const checkAll = document.getElementById("checkAll");
  const otherChecks = document.querySelectorAll("input[name='check']");

  checkAll.addEventListener("change", () => {
    otherChecks.forEach(chk => {
      chk.checked = checkAll.checked;
    });
  });

  // 개별 체크 시 전체동의 해제
  otherChecks.forEach(chk => {
    chk.addEventListener("change", () => {
      const allChecked = Array.from(otherChecks).every(c => c.checked);
      checkAll.checked = allChecked;
    });
  });
  
  const reselectPopup = document.getElementById("reselectPopup");
  const reselectBtn = document.getElementById("reselectBtn");
  const closePopupBtn = document.getElementById("closePopupBtn");

  const infoButtons = document.querySelectorAll(".reservation-info01 button, .reservation-info02 button, .reservation-info03 button");

  infoButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      reselectPopup.style.display = "flex";
    });
  });

  closePopupBtn.addEventListener("click", () => {
    reselectPopup.style.display = "none";
  });

  reselectBtn.addEventListener("click", () => {
    /*<![CDATA[*/
    window.location.href = /*[[@{/diningResv}]]*/ '';
    /*]]>*/
  });
  
  let isPaymentSelected = false;
  let selectedPaymentText = "";

  const requiredInputs = document.querySelectorAll(".input-field");
  const requiredChecks = document.querySelectorAll("input[name='check-required']");
  const payBtn = document.querySelector(".next-btn");
  const payBtnText = payBtn.querySelector("span");
  const onSiteBtn = document.querySelector(".field-btn");
  const onlineBtn = document.querySelector(".online-btn");

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

  // 전체 동의 체크시 validateAll 호출
  checkAll.addEventListener("change", () => {
    const isChecked = checkAll.checked;
    document.querySelectorAll("input[type='checkbox']").forEach(chk => {
  	  chk.checked = isChecked;
    });
    validateAll(); // 전체 동의 시에도 다시 유효성 검사
  });

  otherChecks.forEach(chk => {
    chk.addEventListener("change", () => {
      const allChecked = Array.from(otherChecks).every(c => c.checked);
      checkAll.checked = allChecked;
      validateAll(); // 개별 동의 변경 시에도 유효성 검사
    });
  });

  // 결제 버튼 텍스트 바꾸기
  onSiteBtn.addEventListener("click", () => {
    isPaymentSelected = true;
    selectedPaymentText = "현장결제";
    onSiteBtn.classList.add("selected");
    onlineBtn.classList.remove("selected");
    payBtnText.textContent = selectedPaymentText;
    validateAll();
  });

  onlineBtn.addEventListener("click", () => {
    isPaymentSelected = true;
    selectedPaymentText = "온라인결제";
    onlineBtn.classList.add("selected");
    onSiteBtn.classList.remove("selected");
    payBtnText.textContent = selectedPaymentText;
    validateAll();
  });

  payBtn.addEventListener("click", (e) => {
    if (!payBtn.classList.contains("disabled")) {
      /*<![CDATA[*/
      window.location.href = /*[[@{/diningResvComplete}]]*/ '';
      /*]]>*/
    } else {
      e.preventDefault();
    }
  });
  
});