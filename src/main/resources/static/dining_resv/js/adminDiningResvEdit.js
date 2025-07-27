/**
 * 
 */
function loadTimeSlots() {
  const diningId = $("#diningId").val();
  const mealType = $("#mealType").val();
  const selectedTime = $("#selectedTime").val();

  $.ajax({
    url: "/api/config/timeslots",
    type: "GET",
    data: { diningId, mealType },
    success: function (data) {
      const $timeSelect = $("#reservationTime");
      $timeSelect.empty();

      data.forEach(function (time) {
        const option = $("<option>")
          .val(time)
          .text(time)
          .prop("selected", time === selectedTime);

        $timeSelect.append(option);
      });
    },
    error: function () {
      alert("시간 정보를 불러오는 데 실패했습니다.");
    },
  });
}

$(document).ready(function () {
  loadTimeSlots();

  // mealType 선택 시에도 시간 목록 다시 불러오기
  $("#mealType").on("change", function () {
    loadTimeSlots();
  });
});