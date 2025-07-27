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
  const allCheckboxes = document.querySelectorAll("input[type='checkbox']:not(#checkAll)");
  const requiredInputs = document.querySelectorAll(".input-field");
  const requiredChecks = document.querySelectorAll("input[name='check-required']");
  const payBtn = document.querySelector(".next-btn");
  const payBtnText = payBtn.querySelector("span");
  const paymentMethod = document.getElementById("paymentMethod");
  
  const methodButtons = document.querySelectorAll('.payment-method-btn button');

  methodButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      methodButtons.forEach(b => b.classList.remove('selected'));
      
      btn.classList.add('selected');
    });
  });
  
  const onSiteBtn = document.querySelector(".field-btn");
  const onlineBtn = document.querySelector(".online-btn");
  const reselectPopup = document.getElementById("reselectPopup");
  const reselectBtn = document.getElementById("reselectBtn");
  const closePopupBtn = document.getElementById("closePopupBtn");
  
  const emailInput = document.querySelector('input[name="reservationEmail"]');
  const emailError = emailInput.nextElementSibling;

  emailInput.addEventListener('blur', () => {
    const email = emailInput.value.trim();
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (email === '') {
      // 필수 입력 에러 메시지 보여주기
      emailError.textContent = "이메일을 입력해주세요.";
      emailError.style.display = 'block';
      emailInput.classList.add('error');
    } else if (!emailPattern.test(email)) {
      // 형식 에러 메시지 보여주기
      emailError.textContent = "이메일 형식이 올바르지 않습니다.";
      emailError.style.display = 'block';
      emailInput.classList.add('error');
    } else {
      // 에러 메시지 숨기기
      emailError.style.display = 'none';
      emailInput.classList.remove('error');
    }
  });
  
  const telInput = document.getElementById('reservationTell');

  telInput.addEventListener('input', (e) => {
    let value = e.target.value;
    
    value = value.replace(/[^0-9]/g, '');
    
    if(value.length < 4){
      e.target.value = value;
    } else if(value.length < 8){
      e.target.value = value.slice(0,3) + '-' + value.slice(3);
    } else {
      e.target.value = value.slice(0,3) + '-' + value.slice(3,7) + '-' + value.slice(7,11);
    }
  });
  
  const check04 = document.getElementById("check04");
  const check05 = document.getElementById("check05");
  const check06 = document.getElementById("check06");

  const label05 = check05.closest('label');
  const label06 = check06.closest('label');

  function updateOptionalCheckboxState() {
    const isCheck04Checked = check04.checked;

    if (isCheck04Checked) {
      // 옵션 활성화
      [label05, label06].forEach(label => label.classList.remove("checkbox-disabled"));
      [check05, check06].forEach(chk => {
        chk.disabled = false;
      });
    } else {
      // 옵션 비활성화
      [check05, check06].forEach(chk => {
        chk.checked = false;
        chk.disabled = true;
      });
      [label05, label06].forEach(label => label.classList.add("checkbox-disabled"));
    }
  }

  check04.addEventListener("change", updateOptionalCheckboxState);

  updateOptionalCheckboxState();
  
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

  // 전체 동의 누르면 전체 체크
  checkAll.addEventListener("change", () => {
    const isChecked = checkAll.checked;
    allCheckboxes.forEach(chk => {
      chk.checked = isChecked;
    });
	updateOptionalCheckboxState();
	
    validateAll();
  });

  // 개별 체크 상태 바뀌면 전체 동의 자동 체크/해제
  allCheckboxes.forEach(chk => {
    chk.addEventListener("change", () => {
      const allChecked = Array.from(allCheckboxes).every(c => c.checked);
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
    const diningIdInput = document.querySelector('input[name="diningId"]');
    if (diningIdInput && diningIdInput.value) {
      const diningId = diningIdInput.value;
      window.location.href = `/diningResv?diningId=${diningId}`;
    } else {
      alert("diningId 정보를 찾을 수 없습니다.");
    }
  });

  // 결제 방법 버튼 클릭
  onSiteBtn.addEventListener("click", () => {
    isPaymentSelected = true;
    selectedPaymentText = "현장결제";
    selectedPaymentInput.value = selectedPaymentText;
    onSiteBtn.classList.add("selected");
    onlineBtn.classList.remove("selected");
    payBtnText.textContent = selectedPaymentText;
	paymentMethod.style.display = "none";
    validateAll();
  });

  onlineBtn.addEventListener("click", () => {
    isPaymentSelected = true;
    selectedPaymentText = "온라인결제";
    selectedPaymentInput.value = selectedPaymentText;
    onlineBtn.classList.add("selected");
    onSiteBtn.classList.remove("selected");
    payBtnText.textContent = selectedPaymentText;
	paymentMethod.style.display = "block";
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
    if (payBtn.classList.contains("disabled")) return;

    // 온라인 결제일 경우 → Iamport 실행
    if (selectedPaymentText === "온라인결제" && selectedPaymentMethod !== null) {
      const amountText = document.querySelector(".price-num").textContent;
      const amount = parseInt(amountText.replace(/[₩,]/g, ""));
      const diningName = document.querySelector(".dining-name-txt").textContent.trim();
      const productName = `미미르호텔 ${diningName} 다이닝 예약`;

      let channelKey = "";
      if (selectedPaymentMethod === "카카오페이") {
        channelKey = "channel-key-01764171-b249-4c16-9d18-e9174fa8e611";
      } else if (selectedPaymentMethod === "토스페이") {
        channelKey = "channel-key-01aab9b9-828b-4913-9395-52f922e578f6";
      } else if (selectedPaymentMethod === "카드결제") {
        channelKey = "channel-key-bf43e218-5567-4875-96da-3270e1fba054";
      }

      if (!channelKey) {
        alert("결제수단이 올바르지 않습니다.");
        return;
      }

      IMP.init("imp14397622");

      IMP.request_pay({
        channelKey: channelKey,
        pay_method: "card",
        merchant_uid: "order_" + new Date().getTime(),
        name: productName,
        amount: amount,
        buyer_name: document.querySelector("input[name='reservationName']").value.trim(),
        buyer_tel: document.querySelector("input[name='reservationTell']").value.trim(),
      }, function (rsp) {
        if (rsp.success) {
          showModal("successModal", null, () => {
          document.getElementById("paymentType").value = selectedPaymentMethod;
          document.getElementById("reservationForm").submit();
		  });
        } else {
          showModal("failModal", "결제에 실패했습니다: " + rsp.error_msg);
        }
      });
    } else {
      document.querySelector("#reservationForm").submit();
    }
  });
});

let selectedPaymentMethod = null;

function selectPayment(method, btn) {
  selectedPaymentMethod = method;

  document.querySelectorAll('.payment-method-btn button')
    .forEach(b => b.classList.remove('selected'));

  btn.classList.add('selected');
}

function showModal(modalId, message, callback) {
  const modal = document.getElementById(modalId);
  if (message) {
    const msgEl = modal.querySelector("p");
    msgEl.textContent = message;
  }
  modal.style.display = "block";

  const btn = modal.querySelector("button");
  btn.addEventListener("click", () => {
    modal.style.display = "none";
    if (callback) callback();
  }, { once: true });
}