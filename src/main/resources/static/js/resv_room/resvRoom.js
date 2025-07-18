let confirmedAdults = 2; // 초기 확인된 성인 수
let confirmedChildren = 0; // 초기 확인된 아동 수

function openModal(id) {
  document.getElementById('overlay').style.display = 'block';
  const modal = document.getElementById(id);
  modal.style.display = 'flex';

  if (id === 'guestModal') {
    const selectedCard = document.querySelector('.room-card.selected');
    if (selectedCard) {
      const capacity = parseInt(selectedCard.dataset.capacity);
      document.getElementById('adultCount').setAttribute('max', capacity);
      // 모달 열 때 확인된 값으로 입력 필드 초기화
      document.getElementById('adultCount').value = confirmedAdults;
      document.getElementById('childCount').value = confirmedChildren;
    }
  }
}

function changeGuestCount(delta) {
  const input = document.getElementById("adultCount");
  let value = parseInt(input.value) + delta;
  if (value < 1) value = 1;
  input.value = value;
}

function closeModal() {
  document.getElementById('overlay').style.display = 'none';
  document.querySelectorAll('.modal').forEach(modal => {
    modal.style.display = 'none';
  });
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

  const placeholder = document.querySelector('.guest-placeholder');
  if (placeholder) {
    placeholder.textContent = `성인 ${confirmedAdults}, 아동 ${confirmedChildren}`;
  } else {
    console.error('.guest-placeholder 요소를 찾을 수 없습니다.');
  }

  closeModal();
}


function cancelGuestSelection() {
  document.getElementById('adultCount').value = confirmedAdults;
  document.getElementById('childCount').value = confirmedChildren;
  closeModal();
}
