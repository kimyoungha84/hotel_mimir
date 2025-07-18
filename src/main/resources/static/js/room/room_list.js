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
        defaultDate: [today, tomorrow],
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

function formatQueryDate(date) {
  if (!(date instanceof Date)) date = new Date(date);

  const year = date.getFullYear();
  const month = ('0' + (date.getMonth() + 1)).slice(-2);
  const day = ('0' + date.getDate()).slice(-2);

  return `${year}-${month}-${day}`;
}

function calcNights(start, end) {
  const diffTime = end.getTime() - start.getTime();
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

function updateDateSummary(startDate, endDate) {
  const summary = document.querySelector('.reservation-summary p:nth-child(1)');
  if (startDate && endDate && summary) {
    const formatted = `${formatDate(startDate)} - ${formatDate(endDate)} / ${calcNights(startDate, endDate)}박`;
    summary.innerHTML = `<strong>체크인 / 아웃</strong><br /> ${formatted} <span class="arrow">▸</span>`;
  }
}

function closeModal() {
  document.getElementById('overlay').style.display = 'none';
  document.querySelectorAll('.modal').forEach(modal => {
    modal.style.display = 'none';
  });
}

function confirmDateSelection() {
  const selectedDates = datePickerInstance.selectedDates;
  if (selectedDates.length === 2) {
	const checkIn = formatQueryDate(selectedDates[0]);
	const checkOut = formatQueryDate(selectedDates[1]);

	updateDateSummary(selectedDates[0], selectedDates[1]);

	location.href = "/roomlist?checkIn=" + checkIn + "&checkOut=" + checkOut;
  } else {
    updateDateSummary(today, tomorrow);
  }
  closeModal();
}


function toLocalDateString(date) {
  return date.getFullYear() + '-' +
    (date.getMonth() + 1).toString().padStart(2, '0') + '-' +
    date.getDate().toString().padStart(2, '0');
}

document.addEventListener('DOMContentLoaded', () => {
  // URL에서 날짜 파라미터 추출
  const urlParams = new URLSearchParams(window.location.search);
  const checkInStr = urlParams.get('checkIn');
  const checkOutStr = urlParams.get('checkOut');

  let checkInDate = today;
  let checkOutDate = tomorrow;

  if (checkInStr && checkOutStr) {
    const parsedCheckIn = new Date(checkInStr);
    const parsedCheckOut = new Date(checkOutStr);
    if (!isNaN(parsedCheckIn) && !isNaN(parsedCheckOut)) {
      checkInDate = parsedCheckIn;
      checkOutDate = parsedCheckOut;
    }
  }

  // 날짜 요약 및 flatpickr 초기화에 사용될 날짜 설정
  updateDateSummary(checkInDate, checkOutDate);

  // flatpickr이 아직 openModal() 안 열렸을 경우 대비해서 기본 값 세팅
  if (!datePickerInstance) {
    datePickerInstance = flatpickr("#datepicker", {
      mode: "range",
      dateFormat: "Y-m-d",
      showMonths: 2,
      inline: true,
      locale: "ko",
      defaultDate: [checkInDate, checkOutDate],
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
  }
  document.querySelectorAll('.room-card').forEach(card => {
    card.addEventListener('click', () => {
      document.querySelectorAll('.room-card').forEach(c => c.classList.remove('selected'));
      card.classList.add('selected');
    });
  });

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

  document.querySelectorAll('.sort-option').forEach(option => {
    option.addEventListener('click', () => {
      document.querySelectorAll('.sort-option').forEach(opt => opt.classList.remove('selected'));
      option.classList.add('selected');
      document.querySelector('.sort-accordion .accordion-btn').textContent = `정렬 기준 ▾ (${option.textContent})`;
    });
  });

  document.querySelectorAll('.tag-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.classList.toggle('selected');
    });
  });

  document.querySelectorAll('.reset-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.closest('.accordion-content').querySelectorAll('.tag-btn').forEach(tag => {
        tag.classList.remove('selected');
      });
    });
  });

  const defaultOption = document.querySelector('.sort-option.selected');
  if (defaultOption) {
    document.querySelector('.sort-accordion .accordion-btn').textContent = `정렬 기준 ▾ (${defaultOption.textContent})`;
  }

  document.querySelectorAll('.price-btn').forEach(btn => {
    btn.addEventListener('click', function(event) {
      event.preventDefault();

      const card = this.closest('.room-card');
      const roomId = new URL(this.href).searchParams.get('roomId');

      let checkIn = "";
      let checkOut = "";
      let nights = 0;

      if (datePickerInstance && datePickerInstance.selectedDates.length === 2) {
        checkIn = toLocalDateString(datePickerInstance.selectedDates[0]);
        checkOut = toLocalDateString(datePickerInstance.selectedDates[1]);
        nights = calcNights(datePickerInstance.selectedDates[0], datePickerInstance.selectedDates[1]);
      } else {
        checkIn = toLocalDateString(today);
        checkOut = toLocalDateString(tomorrow);
        nights = calcNights(today, tomorrow);
      }

      const typeName = card.querySelector('h3')?.textContent.trim() || '';
      const capacity = card.dataset.capacity || '';
      const priceText = this.textContent.trim();
      const priceNumber = priceText.replace(/[₩,]/g, '');

      const url = new URL(this.href, window.location.origin);
      url.searchParams.set('checkIn', checkIn);
      url.searchParams.set('checkOut', checkOut);
      url.searchParams.set('nights', nights);
      url.searchParams.set('typeName', typeName);
      url.searchParams.set('pricePerNight', priceNumber);
      url.searchParams.set('capacity', capacity);

      window.location.href = url.toString();
    });
  });
});