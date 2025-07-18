let confirmedAdults = 2; // 초기 확인된 성인 수
let confirmedChildren = 0; // 초기 확인된 아동 수
let isConfirmed = false; // 사용자가 확인 버튼을 눌렀는지 여부

// URL에서 파라미터 값을 읽는 함수
function getParameterByName(name) {
  const url = window.location.href;
  name = name.replace(/[\\[\\]]/g, '\\$&');
  const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
  const results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

function openModal(id) {
  document.getElementById('overlay').style.display = 'block';
  const modal = document.getElementById(id);
  modal.style.display = 'flex';

  if (id === 'guestModal') {
    // URL에서 capacity 읽기, 없으면 기본 10
    const capacity = parseInt(getParameterByName('capacity')) || 10;

    const adultInput = document.getElementById('adultCount');
    adultInput.setAttribute('max', capacity);

    // 입력 필드 초기값 세팅
    adultInput.value = confirmedAdults;
    document.getElementById('childCount').value = confirmedChildren;

    isConfirmed = false;
  }
}

function changeGuestCount(type, delta) {
  const input = document.getElementById(type === 'adult' ? 'adultCount' : 'childCount');
  let value = parseInt(input.value) + delta;

  const max = parseInt(input.getAttribute('max')) || 10;
  const min = type === 'adult' ? 1 : 0;

  if (value < min) value = min;
  if (value > max) value = max;

  input.value = value;
}

function confirmGuestSelection() {
  const adults = parseInt(document.getElementById('adultCount').value);
  const children = parseInt(document.getElementById('childCount').value);

  if (adults < 1) {
    alert("성인 인원은 최소 1명 이상이어야 합니다.");
    return;
  }

  confirmedAdults = adults;
  confirmedChildren = children;
  isConfirmed = true;

  document.getElementById('numGuestsAdult').value = confirmedAdults;
  document.getElementById('numGuestsChild').value = confirmedChildren;

  const placeholder = document.querySelector('.guest-placeholder');
  if (placeholder) {
    placeholder.textContent = `성인 ${confirmedAdults}, 아동 ${confirmedChildren}`;
  }

  closeModal();
}

function cancelGuestSelection() {
  isConfirmed = false;
  closeModal();
}

function closeModal() {
  if (!isConfirmed) {
    // 확인하지 않고 닫으면 이전 값 복원
    document.getElementById('adultCount').value = confirmedAdults;
    document.getElementById('childCount').value = confirmedChildren;
  }

  document.getElementById('overlay').style.display = 'none';
  document.querySelectorAll('.modal').forEach(modal => {
    modal.style.display = 'none';
  });

  isConfirmed = false;
}
