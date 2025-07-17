/**
 * 
 */
const calendarUrl = "/calendar";

  function loadCalendar(year, month) {
    document.querySelector('.scheduler-content-wrap').style.display = 'none';

    $.ajax({
      url: calendarUrl,
      data: { year, month },
      success: data => $('#scheduler').html(data),
      error: () => alert('달력을 불러오는 데 실패했습니다.')
    });
  }

  $(document).on('click', '.calendar-nav', function (e) {
    e.preventDefault();
    const year = $(this).data('year');
    const month = $(this).data('month');
    loadCalendar(year, month);
  });