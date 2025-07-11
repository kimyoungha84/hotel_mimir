const holidays = [
  "2025-01-01", "2025-03-01", "2025-05-05",
  "2025-06-06", "2025-08-15", "2025-10-03",
  "2025-10-09", "2025-12-25"
];

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
        onDayCreate: function(dObj, dStr, fp, dayElem) {
          const date = dayElem.dateObj;

          // 로컬 날짜 문자열 생성
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

document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.room-card').forEach(card => {
    card.addEventListener('click', () => {
      const roomType = card.querySelector('h3')?.textContent || '';
      updateGuestSummary(roomType);
    });
  });
});

// 초기 날짜 설정
document.addEventListener('DOMContentLoaded', () => {
  const today = new Date();
  const tomorrow = new Date();
  tomorrow.setDate(today.getDate() + 1);
  updateDateSummary(today, tomorrow);
});



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



document.addEventListener('DOMContentLoaded', () => {
    // 아코디언 버튼 클릭 시 toggle
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

    
    // 정렬 옵션 선택
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
  });

document.addEventListener('DOMContentLoaded', () => {
  const defaultOption = document.querySelector('.sort-option.selected');
  if (defaultOption) {
    document.querySelector('.sort-accordion .accordion-btn').textContent = `정렬 기준 ▾ (${defaultOption.textContent})`;
  }
});

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

document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.price-btn').forEach(btn => {
    btn.addEventListener('click', function(event) {
      event.preventDefault(); // 기본 a 링크 이동 막기

      const card = this.closest('.room-card'); // 🔥 여기가 빠졌었음!
      const roomId = new URL(this.href).searchParams.get('roomId');

      // 날짜 선택값
      let checkIn = "";
      let checkOut = "";
      if (datePickerInstance && datePickerInstance.selectedDates.length === 2) {
        const toISO = date => date.toISOString().slice(0, 10);
        checkIn = toISO(datePickerInstance.selectedDates[0]);
        checkOut = toISO(datePickerInstance.selectedDates[1]);
      }

      const typeName = card.querySelector('h3')?.textContent.trim() || '';

      // 인원 수
      const adult = document.getElementById('adultCount')?.value || "2";
      const child = document.getElementById('childCount')?.value || "0";

      // URL 생성
      const url = new URL(this.href, window.location.origin);
      url.searchParams.set('checkIn', checkIn);
      url.searchParams.set('checkOut', checkOut);
      url.searchParams.set('adult', adult);
      url.searchParams.set('child', child);
      url.searchParams.set('typeName', typeName);

      window.location.href = url.toString();
    });
  });
});


