/**
 * 
 */
function updateTimeOptions() {
  const type = document.getElementById("mealType").value;
  const timeSelect = document.getElementById("reservationTime");

  timeSelect.innerHTML = "";

  let startHour, endHour;
  if (type === "lunch") {
    startHour = 12;
    endHour = 17;
  } else {
    startHour = 18;
    endHour = 21;
  }

  for (let hour = startHour; hour <= endHour; hour++) {
      let hourStr = hour.toString().padStart(2, '0');
      let option1 = document.createElement("option");
      option1.value = `${hourStr}:00:00`;
      option1.text = `${hour}시 00분`;
      timeSelect.appendChild(option1);

      if (hour !== endHour) {
        let option2 = document.createElement("option");
        option2.value = `${hourStr}:30:00`;
        option2.text = `${hour}시 30분`;
        timeSelect.appendChild(option2);
      }
    }
  }

  document.addEventListener("DOMContentLoaded", function() {
  const timeSelect = document.getElementById("reservationTime");
  const selectedTime = timeSelect.getAttribute("data-selected-time");
  const hour = parseInt(selectedTime?.substring(0, 2), 10);

  const mealType = document.getElementById("mealType");
  if (hour >= 12 && hour <= 17) {
    mealType.value = "lunch";
  } else {
    mealType.value = "dinner";
  }

  updateTimeOptions();

  if (selectedTime) {
    Array.from(timeSelect.options).forEach(opt => {
      if (opt.value === selectedTime) {
        opt.selected = true;
      }
    });
  }
});