const holidays = [
  "2025-01-01", "2025-03-01", "2025-05-05",
  "2025-06-06", "2025-08-15", "2025-10-03",
  "2025-10-09", "2025-12-25"
];

const today = new Date();
const tomorrow = new Date();
tomorrow.setDate(today.getDate() + 1);

let datePickerInstance = null;

function openModal(id) {
  document.getElementById('overlay').style.display = 'block';
  const modal = document.getElementById(id);
  modal.style.display = 'flex';

  if (id === 'dateModal') {
    setTimeout(() => {
      if (datePickerInstance) datePickerInstance.destroy();

      datePickerInstance = flatpickr("#datepicker", {
        mode: "range",
        dateFormat: "Y-m-d",
        showMonths: 2,
        inline: true,
        locale: "ko",
        defaultDate: [today, tomorrow],  // 초기 날짜 설정
        onDayCreate: function(dObj, dStr, fp, dayElem) {
          const date = dayElem.dateObj;

          const y = date.getFullYear();
          const m = (date.getMonth() + 1).toString().padStart(2, '0');
          const d = date.getDate().toString().padStart(2, '0');
          const yyyymmdd = `${y}-${m}-${d}`;

          if (holidays.includes(yyyymmdd)) {
            dayElem.classList.add("holiday");
          }

          const day = date.getDay();
          dayElem.classList.add("weekday" + day);
        }
      });
    }, 10);
  }
}

function formatDate(date) {
  const days = ['일', '월', '화', '수', '목', '금', '토'];
  const mm = (date.getMonth() + 1).toString().padStart(2, '0');
  const dd = date.getDate().toString().padStart(2, '0');
  const day = days[date.getDay()];
  return `${mm}월 ${dd}일(${day})`;
}

function calcNights(start, end) {
  const diffTime = end.getTime() - start.getTime();
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

function updateDateSummary(startDate, endDate) {
  const summary = document.querySelector('.reservation-summary p:nth-child(1)');
  if (startDate && endDate) {
    const formatted = `${formatDate(startDate)} - ${formatDate(endDate)} / ${calcNights(startDate, endDate)}박`;
    summary.innerHTML = `<strong>체크인 / 아웃</strong><br /> ${formatted} <span class="arrow">▸</span>`;
  }
}

function updateGuestSummary(roomType) {
  const summary = document.querySelector('.reservation-summary p:nth-child(3)');
  summary.innerHTML = `<strong>객실 / 인원</strong><br /> ${roomType} / 성인 2 <span class="arrow">▸</span>`;
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
  const adults = document.getElementById('adultCount').value;
  const children = document.getElementById('childCount').value;

  const summary = document.querySelector('.reservation-summary p:nth-child(2)');
  summary.innerHTML = `<strong>인원</strong><br /> 성인 ${adults}, 아동 ${children} <span class="arrow">▸</span>`;

  closeModal();
}

function confirmDateSelection() {
  const selectedDates = datePickerInstance.selectedDates;
  if (selectedDates.length === 2) {
    updateDateSummary(selectedDates[0], selectedDates[1]);
  }
  closeModal();
}

// DOMContentLoaded 이벤트 중복 최소화해서 한 번에 처리
document.addEventListener('DOMContentLoaded', () => {
  // 초기 날짜 요약 표시
  updateDateSummary(today, tomorrow);

  // 룸 카드 클릭 시 객실명 갱신
  document.querySelectorAll('.room-card').forEach(card => {
    card.addEventListener('click', () => {
      const roomType = card.querySelector('h3')?.textContent || '';
      updateGuestSummary(roomType);
    });
  });

  // 아코디언 버튼 클릭 토글
  document.querySelectorAll('.accordion-btn').forEach((btn) => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.accordion').forEach(acc => {
        if (acc !== btn.parentElement) {
          acc.classList.remove('active');
        }
      });
      btn.parentElement.classList.toggle('active');
    });
  });

  // 정렬 옵션 클릭
  document.querySelectorAll('.sort-option').forEach(option => {
    option.addEventListener('click', () => {
      document.querySelectorAll('.sort-option').forEach(opt => opt.classList.remove('selected'));
      option.classList.add('selected');
      document.querySelector('.sort-accordion .accordion-btn').textContent = `정렬 기준 ▾ (${option.textContent})`;
    });
  });

  // 태그 버튼 선택/해제
  document.querySelectorAll('.tag-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.classList.toggle('selected');
    });
  });

  // 선택해제 버튼 동작
  document.querySelectorAll('.reset-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.closest('.accordion-content').querySelectorAll('.tag-btn').forEach(tag => {
        tag.classList.remove('selected');
      });
    });
  });

  // 기본 정렬 옵션 표시 업데이트
  const defaultOption = document.querySelector('.sort-option.selected');
  if (defaultOption) {
    document.querySelector('.sort-accordion .accordion-btn').textContent = `정렬 기준 ▾ (${defaultOption.textContent})`;
  }

  // 예약 버튼 클릭 이벤트 (날짜/인원/객실명 전달)
  document.querySelectorAll('.price-btn').forEach(btn => {
    btn.addEventListener('click', function(event) {
      event.preventDefault();

      const card = this.closest('.room-card');
      const roomId = new URL(this.href).searchParams.get('roomId');

      let checkIn = "";
      let checkOut = "";
      let nights = 0;

      if (datePickerInstance && datePickerInstance.selectedDates.length === 2) {
        const toISO = date => date.toISOString().slice(0, 10);
        checkIn = toISO(datePickerInstance.selectedDates[0]);
        checkOut = toISO(datePickerInstance.selectedDates[1]);
        nights = calcNights(datePickerInstance.selectedDates[0], datePickerInstance.selectedDates[1]);
      } else {
        // 만약 선택 안 됐으면 기본값으로 today~tomorrow 강제 지정
        checkIn = today.toISOString().slice(0, 10);
        checkOut = tomorrow.toISOString().slice(0, 10);
        nights = calcNights(today, tomorrow);
      }

      const typeName = card.querySelector('h3')?.textContent.trim() || '';
      const adult = document.getElementById('adultCount')?.value || "2";
      const child = document.getElementById('childCount')?.value || "0";
	  
	  const priceText = this.textContent.trim();
	  const priceNumber = priceText.replace(/[₩,]/g, '');
	  

      const url = new URL(this.href, window.location.origin);
      url.searchParams.set('checkIn', checkIn);
      url.searchParams.set('checkOut', checkOut);
      url.searchParams.set('nights', nights);
      url.searchParams.set('adult', adult);
      url.searchParams.set('child', child);
      url.searchParams.set('typeName', typeName);
	  url.searchParams.set('pricePerNight', priceNumber);

      window.location.href = url.toString();
    });
  });
});
