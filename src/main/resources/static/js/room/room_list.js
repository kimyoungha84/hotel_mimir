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