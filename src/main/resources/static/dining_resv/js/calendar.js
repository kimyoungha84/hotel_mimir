/**
 * 
 */
const calendarUrl = "/calendar";

  function loadCalendar(year, month) {
    document.querySelector('.scheduler-content-wrap').style.display = 'none';

	  $.ajax({
	    url: calendarUrl,
	    data: { year, month },
	    success: data => {
	      $('#scheduler').html(data);

	      // 오늘 날짜 중복 표시 제거
	      const todayEls = document.querySelectorAll('.today-circle');
	      if (todayEls.length > 1) {
	        [...todayEls].slice(1).forEach(el => el.style.display = 'none');
	      }
	    },
	    error: () => alert('달력을 불러오는 데 실패했습니다.')
	  });
	}

  $(document).on('click', '.calendar-nav', function (e) {
    e.preventDefault();
	console.log("✅ calendar-nav clicked");
    const year = $(this).data('year');
    const month = $(this).data('month');
    loadCalendar(year, month);
  });