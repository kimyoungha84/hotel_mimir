// 다이닝 상세 페이지 JavaScript

// 갤러리 관련 변수들
let activeIndex = 0;
let galleryImageList = [];

// 예약 관련 변수들
let availableDiningList = [];
let selectedDiningId = null;
let selectedDiningName = null;
let tempSelectedDiningId = null;
let tempSelectedDiningName = null;
let hasSelectedDining = false;

// 페이지 로드 시 초기화
window.addEventListener('DOMContentLoaded', function() {
  // 갤러리 이미지 리스트 초기화
  initGallery();
  
  // 예약 패널 이벤트 리스너 등록
  initReservationPanel();
  
  // 이미지 로드 실패 처리
  initImageErrorHandling();
});

// 이미지 로드 실패 처리 초기화
function initImageErrorHandling() {
  // 메인 이미지
  const mainImg = document.querySelector('.main-img img');
  if (mainImg) {
    mainImg.addEventListener('error', function() {
      this.style.display = 'none';
      const errorDiv = document.createElement('div');
      errorDiv.className = 'image-error-message';
      errorDiv.style.cssText = 'width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#aaa; font-size:1.1rem; background:#f5f5f5;';
      errorDiv.textContent = '이미지 준비중입니다';
      this.parentNode.appendChild(errorDiv);
    });
  }
  
  // 로고 이미지
  const logoImg = document.querySelector('.category-img img');
  if (logoImg) {
    logoImg.addEventListener('error', function() {
      this.style.display = 'none';
      const errorDiv = document.createElement('div');
      errorDiv.className = 'image-error-message';
      errorDiv.style.cssText = 'width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#aaa; font-size:1.1rem; background:#f5f5f5;';
      errorDiv.textContent = '이미지 준비중입니다';
      this.parentNode.appendChild(errorDiv);
    });
  }
  
  // 갤러리 이미지들
  const galleryImgs = document.querySelectorAll('.gallery-image-area img');
  galleryImgs.forEach(img => {
    img.addEventListener('error', function() {
      this.style.display = 'none';
      const errorDiv = document.createElement('div');
      errorDiv.className = 'image-error-message';
      errorDiv.style.cssText = 'width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#aaa; font-size:1.1rem; background:#f5f5f5; border-radius:1rem;';
      errorDiv.textContent = '이미지 준비중입니다';
      this.parentNode.appendChild(errorDiv);
    });
  });
}

// 갤러리 초기화
function initGallery() {
  // Thymeleaf에서 span으로 렌더링한 것을 JS 배열로 변환
  galleryImageList = Array.from(document.querySelectorAll('#gallery-image-list span')).map(span => span.textContent);
  
  const mainImg = document.getElementById('gallery-main-img');
  const currentSpan = document.getElementById('gallery-current');
  const totalSpan = document.getElementById('gallery-total');
  const prevBtn = document.getElementById('gallery-prev');
  const nextBtn = document.getElementById('gallery-next');
  const progress = document.getElementById('gallery-progress');

  function updateGallery() {
    // 기존 에러 메시지 제거
    const existingError = mainImg.parentNode.querySelector('.image-error-message');
    if (existingError) {
      existingError.remove();
    }
    
    // 이미지 표시 상태로 복원
    mainImg.style.display = 'block';
    
    mainImg.src = galleryImageList[activeIndex];
    currentSpan.innerText = activeIndex + 1;
    totalSpan.innerText = galleryImageList.length;
    prevBtn.disabled = activeIndex === 0;
    nextBtn.disabled = activeIndex === galleryImageList.length - 1;
    progress.style.width = ((activeIndex + 1) / galleryImageList.length * 100) + '%';
  }

  prevBtn.onclick = function() {
    if (activeIndex > 0) { 
      activeIndex--; 
      updateGallery(); 
    }
  };
  
  nextBtn.onclick = function() {
    if (activeIndex < galleryImageList.length - 1) { 
      activeIndex++; 
      updateGallery(); 
    }
  };
  
  if(galleryImageList.length > 0) {
    updateGallery();
  } else {
    mainImg.src = '/dining/images/no_image.png';
    currentSpan.innerText = 0;
    totalSpan.innerText = 0;
    prevBtn.disabled = true;
    nextBtn.disabled = true;
    progress.style.width = '0%';
  }
}

// 예약 패널 초기화
function initReservationPanel() {
  // 예약 패널 열기 이벤트
  document.querySelector('.floating-reserve-btn').addEventListener('click', function(event) {
    openReservationPanel(event);
  });
}

// 예약 패널 관련 함수들
function openReservationPanel(event) {
  // 기본 동작 방지 (화면 최상단으로 이동 방지)
  if (event) {
    event.preventDefault();
  }
  
  document.getElementById('reservation-panel').classList.add('active');
  document.getElementById('overlay').classList.add('active');
  fetchAvailableDiningList();
  updateReservationBtnState();
}

function closeReservationPanel() {
  const reservationPanel = document.getElementById('reservation-panel');
  const diningModal = document.getElementById('dining-modal');
  const guestModal = document.getElementById('guest-modal');
  const overlay = document.getElementById('overlay');
  
  // 예약 패널 닫기
  reservationPanel.classList.remove('active');
  
  // 열려있는 모달들도 함께 닫기
  diningModal.classList.remove('active');
  guestModal.classList.remove('active');
  overlay.classList.remove('active');
  
  // 애니메이션 완료 후 모달들 완전히 숨기기
  setTimeout(() => {
    if (!diningModal.classList.contains('active')) {
      diningModal.style.visibility = 'hidden';
    }
    if (!guestModal.classList.contains('active')) {
      guestModal.style.visibility = 'hidden';
    }
  }, 300);
}

function openDiningModal() {
  if (document.getElementById('reservation-panel').classList.contains('active')) {
    closeGuestModal();
    const modal = document.getElementById('dining-modal');
    modal.style.visibility = 'visible';
    modal.classList.add('active');
    document.getElementById('overlay').classList.add('active');
    // 임시 선택값을 현재 선택값으로 초기화
    tempSelectedDiningId = selectedDiningId;
    tempSelectedDiningName = selectedDiningName;
    // 선택 표시도 동기화
    document.querySelectorAll('.dining-option').forEach(opt => {
      if (opt.getAttribute('data-dining-id') == tempSelectedDiningId) {
        opt.classList.add('selected');
      } else {
        opt.classList.remove('selected');
      }
    });
  }
}

function closeDiningModal() {
  const modal = document.getElementById('dining-modal');
  modal.classList.remove('active');
  
  // 애니메이션 완료 후 완전히 숨기기
  setTimeout(() => {
    if (!modal.classList.contains('active')) {
      modal.style.visibility = 'hidden';
    }
  }, 300);
  
  document.getElementById('overlay').classList.remove('active');
}

function openGuestModal() {
  // 예약 패널이 열려있을 때만 인원수 모달 열기
  if (document.getElementById('reservation-panel').classList.contains('active')) {
    // 다른 모달이 열려있으면 닫기
    closeDiningModal();
    
    const modal = document.getElementById('guest-modal');
    modal.style.visibility = 'visible'; // visibility 초기화
    modal.classList.add('active');
    document.getElementById('overlay').classList.add('active');
  }
}

function closeGuestModal() {
  const modal = document.getElementById('guest-modal');
  modal.classList.remove('active');
  
  // 애니메이션 완료 후 완전히 숨기기
  setTimeout(() => {
    if (!modal.classList.contains('active')) {
      modal.style.visibility = 'hidden';
    }
  }, 300);
  
  document.getElementById('overlay').classList.remove('active');
}

function closeAllModals() {
  const reservationPanel = document.getElementById('reservation-panel');
  const diningModal = document.getElementById('dining-modal');
  const guestModal = document.getElementById('guest-modal');
  const overlay = document.getElementById('overlay');
  
  // 모든 모달 닫기
  reservationPanel.classList.remove('active');
  diningModal.classList.remove('active');
  guestModal.classList.remove('active');
  overlay.classList.remove('active');
  
  // 애니메이션 완료 후 완전히 숨기기
  setTimeout(() => {
    if (!diningModal.classList.contains('active')) {
      diningModal.style.visibility = 'hidden';
    }
    if (!guestModal.classList.contains('active')) {
      guestModal.style.visibility = 'hidden';
    }
  }, 300);
}

// 예약 가능 다이닝 옵션 동적 로딩
function renderDiningOptions() {
  const list = document.getElementById('dining-options-list');
  list.innerHTML = '';
  availableDiningList.forEach((dining, idx) => {
    const div = document.createElement('div');
    div.className = 'dining-option' + (selectedDiningId == dining.dining_id ? ' selected' : '');
    div.setAttribute('data-dining-id', dining.dining_id);
    div.onclick = function() { selectDiningOption(dining.dining_id, dining.dining_name, div); };
    div.innerHTML = `
      <img src="${dining.dining_main_image || '/dining/images/no_image.png'}" alt="${dining.dining_name}" style="width:80px;height:60px;object-fit:cover;border-radius:4px;margin-right:15px;">
      <div class="dining-details">
        <h4>${dining.dining_name}</h4>
        <p>타입 | ${dining.type || ''}</p>
        <p>위치 | ${dining.location || ''}</p>
      </div>
      <div class="selection-indicator"></div>
    `;
    list.appendChild(div);
  });
}

function updateReservationBtnState() {
  const btn = document.getElementById('reservation-search-btn');
  const diningSelected = !!selectedDiningId;
  const adult = parseInt(document.getElementById('adult-count').textContent) || 0;
  const child = parseInt(document.getElementById('child-count').textContent) || 0;
  const total = adult + child;
  btn.disabled = !(diningSelected && total > 0);
}

// 다이닝 선택 시 버튼 상태 갱신
function selectDiningOption(diningId, diningName, element) {
  document.querySelectorAll('.dining-option').forEach(opt => opt.classList.remove('selected'));
  element.classList.add('selected');
  tempSelectedDiningId = diningId;
  tempSelectedDiningName = diningName;
  // hasSelectedDining은 "선택" 버튼에서만 true로!
  updateReservationBtnState();
}

function confirmDiningSelection() {
  if (tempSelectedDiningName) {
    selectedDiningId = tempSelectedDiningId;
    selectedDiningName = tempSelectedDiningName;
    hasSelectedDining = true;
    document.getElementById('selected-dining-name').textContent = selectedDiningName;
    closeDiningModal();
    updateReservationBtnState();
  }
}

function fetchAvailableDiningList() {
  fetch('/user/available_dining_list')
    .then(res => res.json())
    .then(data => {
      availableDiningList = data;
      // 기본 선택값: 없음(처음엔 선택 안 함)
      if (!hasSelectedDining) {
        selectedDiningId = null;
        selectedDiningName = null;
        document.getElementById('selected-dining-name').textContent = '선택해주세요';
      } else if (selectedDiningId && availableDiningList.some(d => d.dining_id == selectedDiningId)) {
        document.getElementById('selected-dining-name').textContent = selectedDiningName;
      }
      renderDiningOptions();
    });
}

// 예약 검색 함수 (diningId 포함)
function searchReservation() {
  const diningId = selectedDiningId;
  const diningName = selectedDiningName;
  const adultCount = document.getElementById('adult-count').textContent;
  const childCount = document.getElementById('child-count').textContent;
  
  if (!diningId) {
    alert('다이닝을 선택해 주세요.');
    return;
  }
  // 예시: 예약 페이지로 이동 (diningId 포함)
  window.location.href = `/diningResv?diningId=${encodeURIComponent(diningId)}&adult=${adultCount}&child=${childCount}`;
}

// 인원수 관련 함수들
function increaseCount(type) {
  const counter = document.getElementById(type + '-counter');
  const count = document.getElementById(type + '-count');
  let currentValue = parseInt(counter.textContent);
  
  if (type === 'adult' && currentValue < 10) {
    currentValue++;
  } else if (type === 'child' && currentValue < 10) {
    currentValue++;
  }
  
  counter.textContent = currentValue;
  count.textContent = currentValue;
  updateReservationBtnState();
}

function decreaseCount(type) {
  const counter = document.getElementById(type + '-counter');
  const count = document.getElementById(type + '-count');
  let currentValue = parseInt(counter.textContent);
  
  if (currentValue > 0) {
    currentValue--;
    counter.textContent = currentValue;
    count.textContent = currentValue;
    updateReservationBtnState();
  }
}

function confirmGuestSelection() {
  closeGuestModal();
  updateReservationBtnState();
} 