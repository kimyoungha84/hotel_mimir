// 대표메뉴 관리 페이지 JavaScript

let selectedMenuId = null;
let diningId = 0;
let descriptions = [];

// 디버깅용 로그
console.log('=== Frontend Debug Info ===');
console.log('DiningId:', diningId);
console.log('Descriptions:', descriptions);
console.log('Descriptions length:', descriptions.length);
console.log('===========================');

// 분류 셀렉트 변경 이벤트 처리 함수
function handleDescriptionSelectChange(selectId, inputId) {
  const select = document.getElementById(selectId);
  const input = document.getElementById(inputId);
  
  if (select.value === 'new') {
    input.style.display = 'block';
    input.focus();
    input.value = '';
  } else {
    input.style.display = 'none';
    input.value = select.value;
  }
}

// 페이지 로드 시 이벤트 리스너 등록
document.addEventListener('DOMContentLoaded', function() {
  // Thymeleaf에서 전달받은 데이터 설정
  diningId = window.diningId || 0;
  descriptions = window.descriptions || [];
  
  const tbody = document.getElementById('resultArea');
  tbody.addEventListener('click', function(e) {
    let tr = e.target.closest('tr');
    if (!tr || !tr.getAttribute('data-menu-id')) return;
    Array.from(tbody.querySelectorAll('tr')).forEach(r => r.classList.remove('highlight-row'));
    tr.classList.add('highlight-row');
    window.selectedMenuId = tr.getAttribute('data-menu-id');
    console.log('row click:', window.selectedMenuId);
  });
  
  // 삭제 버튼 이벤트
  document.getElementById('deleteBtn').addEventListener('click', function(e) {
    e.preventDefault();
    if (!window.selectedMenuId) {
      alert('레코드를 선택하세요.');
      return;
    }
    if (!confirm('정말 삭제하시겠습니까?')) {
      return;
    }
    axios.delete('/api/admin/repMenu/' + window.selectedMenuId)
      .then(function(response) {
        if (response.data.success) {
          alert('삭제가 완료되었습니다.');
          location.reload();
        } else {
          alert('삭제에 실패했습니다: ' + response.data.message);
        }
        window.selectedMenuId = null;
      })
      .catch(function(error) {
        alert('삭제에 실패했습니다. 참조된 데이터가 있을 수 있습니다.');
        console.error(error);
      });
  });
  
  // 수정 버튼 클릭 이벤트 (테이블 내 각 행의 수정 버튼)
  document.querySelectorAll('.edit-menu-btn').forEach(button => {
    button.addEventListener('click', function(e) {
      e.preventDefault();
      const menuId = this.getAttribute('data-menu-id');
      const menuName = this.getAttribute('data-menu-name');
      const description = this.getAttribute('data-description');
      const price = this.getAttribute('data-price');

      // 팝업 필드에 데이터 채우기
      document.getElementById('editDiningId').value = diningId;
      document.getElementById('editMenuId').value = menuId;
      document.getElementById('editMenuName').value = menuName;
      document.getElementById('editPrice').value = price;

      // 분류 설정
      const editSelect = document.getElementById('editDescriptionSelect');
      const editInput = document.getElementById('editDescriptionInput');
      
      // 기존 분류 목록에 있는지 확인
      const existingDescriptions = Array.from(editSelect.options).map(option => option.value);
      if (existingDescriptions.includes(description)) {
        editSelect.value = description;
        editInput.style.display = 'none';
        editInput.value = description;
      } else {
        editSelect.value = 'new';
        editInput.style.display = 'block';
        editInput.value = description;
      }

      // 모달 열기
      document.getElementById('editRepMenuModal').style.display = 'block';
    });
  });

  // 수정 모달의 분류 셀렉트 변경 이벤트
  document.getElementById('editDescriptionSelect').addEventListener('change', function() {
    handleDescriptionSelectChange('editDescriptionSelect', 'editDescriptionInput');
  });

  // 추가 모달의 분류 셀렉트 변경 이벤트
  document.getElementById('addDescriptionSelect').addEventListener('change', function() {
    handleDescriptionSelectChange('addDescriptionSelect', 'addDescriptionInput');
  });

  // 모달 닫기 버튼 이벤트
  document.getElementById('closeEditModalBtn').addEventListener('click', function() {
    document.getElementById('editRepMenuModal').style.display = 'none';
  });

  // 모달 외부 클릭 시 닫기
  window.addEventListener('click', function(event) {
    const addModal = document.getElementById('addRepMenuModal');
    const editModal = document.getElementById('editRepMenuModal');
    if (event.target == addModal) {
      addModal.style.display = 'none';
    }
    if (event.target == editModal) {
      editModal.style.display = 'none';
    }
  });

  // X 버튼 클릭 이벤트 (수정 모달)
  document.querySelector('#editRepMenuModal .close-button').addEventListener('click', function() {
    document.getElementById('editRepMenuModal').style.display = 'none';
  });

  // X 버튼 클릭 이벤트 (추가 모달)
  document.querySelector('#addRepMenuModal .close-button').addEventListener('click', function() {
    document.getElementById('addRepMenuModal').style.display = 'none';
  });

  // 팝업 내 수정 폼 제출 이벤트
  document.getElementById('editRepMenuForm').addEventListener('submit', function(e) {
    e.preventDefault();

    // 유효성검증
    const menuName = document.getElementById('editMenuName').value.trim();
    const descriptionSelect = document.getElementById('editDescriptionSelect');
    const descriptionInput = document.getElementById('editDescriptionInput');
    const price = document.getElementById('editPrice').value.trim();

    if (!menuName) {
      alert('메뉴명을 입력해주세요.');
      document.getElementById('editMenuName').focus();
      return;
    }

    let description = '';
    if (descriptionSelect.value === 'new') {
      description = descriptionInput.value.trim();
      if (!description) {
        alert('새로운 분류명을 입력해주세요.');
        descriptionInput.focus();
        return;
      }
    } else {
      description = descriptionSelect.value;
      if (!description) {
        alert('분류를 선택해주세요.');
        descriptionSelect.focus();
        return;
      }
    }

    if (!price) {
      alert('가격을 입력해주세요.');
      document.getElementById('editPrice').focus();
      return;
    }

    if (price < 0) {
      alert('가격은 0 이상이어야 합니다.');
      document.getElementById('editPrice').focus();
      return;
    }

    const formData = new FormData(this);
    const data = {};
    formData.forEach((value, key) => {
      data[key] = value;
    });

    axios.put('/api/admin/repMenu/update', data)
      .then(response => {
        if (response.data.success) {
          alert('대표메뉴가 성공적으로 수정되었습니다.');
          document.getElementById('editRepMenuModal').style.display = 'none';
          location.reload();
        } else {
          alert('대표메뉴 수정에 실패했습니다: ' + response.data.message);
        }
      })
      .catch(error => {
        console.error('Error updating rep menu:', error);
        alert('대표메뉴 수정 중 오류가 발생했습니다.');
      });
  });

  // 추가 버튼 클릭 이벤트
  document.getElementById('addMenuBtn').addEventListener('click', function() {
    // 추가 모달 필드 초기화
    document.getElementById('addDiningId').value = diningId;
    document.getElementById('addMenuName').value = '';
    document.getElementById('addDescriptionSelect').value = '';
    document.getElementById('addDescriptionInput').value = '';
    document.getElementById('addDescriptionInput').style.display = 'none';
    document.getElementById('addPrice').value = '';

    // 추가 모달 열기
    document.getElementById('addRepMenuModal').style.display = 'block';
  });

  // 추가 모달 닫기 버튼 이벤트
  document.getElementById('closeAddModalBtn').addEventListener('click', function() {
    document.getElementById('addRepMenuModal').style.display = 'none';
  });

  // 추가 모달 폼 제출 이벤트
  document.getElementById('addRepMenuForm').addEventListener('submit', function(e) {
    e.preventDefault();

    // 유효성검증
    const menuName = document.getElementById('addMenuName').value.trim();
    const descriptionSelect = document.getElementById('addDescriptionSelect');
    const descriptionInput = document.getElementById('addDescriptionInput');
    const price = document.getElementById('addPrice').value.trim();

    if (!menuName) {
      alert('메뉴명을 입력해주세요.');
      document.getElementById('addMenuName').focus();
      return;
    }

    let description = '';
    if (descriptionSelect.value === 'new') {
      description = descriptionInput.value.trim();
      if (!description) {
        alert('새로운 분류명을 입력해주세요.');
        descriptionInput.focus();
        return;
      }
    } else {
      description = descriptionSelect.value;
      if (!description) {
        alert('분류를 선택해주세요.');
        descriptionSelect.focus();
        return;
      }
    }

    if (!price) {
      alert('가격을 입력해주세요.');
      document.getElementById('addPrice').focus();
      return;
    }

    if (price < 0) {
      alert('가격은 0 이상이어야 합니다.');
      document.getElementById('addPrice').focus();
      return;
    }

    const formData = new FormData(this);
    const data = {};
    formData.forEach((value, key) => {
      data[key] = value;
    });

    axios.post('/api/admin/repMenu/add', data)
      .then(response => {
        if (response.data.success) {
          alert('대표메뉴가 성공적으로 추가되었습니다.');
          document.getElementById('addRepMenuModal').style.display = 'none';
          location.reload();
        } else {
          alert('대표메뉴 추가에 실패했습니다: ' + response.data.message);
        }
      })
      .catch(error => {
        console.error('Error adding rep menu:', error);
        alert('대표메뉴 추가 중 오류가 발생했습니다.');
      });
  });
}); 