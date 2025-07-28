// 다이닝 관리 페이지 JavaScript

// 반드시 전역에 선언
let selectedDiningId = null;

document.addEventListener('DOMContentLoaded', function() {
  const tbody = document.getElementById('resultArea');
  tbody.addEventListener('click', function(e) {
    let tr = e.target.closest('tr');
    if (!tr || !tr.getAttribute('data-dining-id')) return;
    Array.from(tbody.querySelectorAll('tr')).forEach(r => r.classList.remove('highlight-row'));
    tr.classList.add('highlight-row');
    window.selectedDiningId = tr.getAttribute('data-dining-id');
    console.log('row click:', window.selectedDiningId);
  });

  document.getElementById('repMenuEditBtn').addEventListener('click', function(e) {
    if (!window.selectedDiningId) {
      alert('대표메뉴를 수정할 다이닝을 선택하세요.');
      e.preventDefault();
      return;
    }
    window.location.href = '/admin/repMenu?diningId=' + window.selectedDiningId;
    e.preventDefault();
  });

  document.getElementById('deleteBtn').addEventListener('click', function(e) {
    e.preventDefault();
    if (!window.selectedDiningId) {
      alert('삭제할 다이닝을 선택하세요.');
      return;
    }
    if (!confirm('정말 삭제하시겠습니까?')) {
      return;
    }
    axios.delete('/admin/dining/' + window.selectedDiningId)
      .then(function(response) {
        alert('삭제가 완료되었습니다.');
        // 테이블 갱신 (예: 검색 버튼 클릭 트리거)
        // location.reload(); // 또는 아래처럼 fragment만 갱신
        document.getElementById('searchApiBtn')?.click();
        window.selectedDiningId = null;
      })
      .catch(function(error) {
        alert('삭제에 실패했습니다. 참조된 데이터가 있을 수 있습니다.');
        console.error(error);
      });
  });
}); 