// 다이닝 수정 페이지 JavaScript

let deletedCarouselUrls = [];
let replacedCarouselFiles = {};
let carouselOrder = [];
let mainImageFile = null;
let logoImageFile = null;

// 유효성 검사 함수들
function validateDiningForm() {
  const errors = [];
  
  // 1. 필수 필드 검사
  const requiredFields = {
    'dining_name': '이름',
    'type': '타입', 
    'location': '위치',
    'phone_number': '문의번호',
    'manager_name': '담당자',
    'dining_classification': '분류'
  };
  
  for (const [fieldName, fieldLabel] of Object.entries(requiredFields)) {
    const field = document.querySelector(`[name="${fieldName}"]`);
    if (!field || !field.value.trim()) {
      errors.push(`${fieldLabel}은(는) 필수 입력 항목입니다.`);
    }
  }
  
  // 2. 데이터 길이 검사 (한글 기준으로 조정)
  const lengthValidations = {
    'dining_name': { max: 66, label: '이름' }, // 200바이트 ÷ 3바이트 = 약 66자
    'type': { max: 33, label: '타입' }, // 100바이트 ÷ 3바이트 = 약 33자
    'location': { max: 6, label: '위치' }, // 20바이트 ÷ 3바이트 = 약 6자
    'phone_number': { max: 33, label: '문의번호' }, // 100바이트 ÷ 3바이트 = 약 33자
    'manager_name': { max: 5, label: '담당자' }, // 15바이트 ÷ 3바이트 = 5자
    'dining_introduction': { max: 333, label: '소개' }, // 1000바이트 ÷ 3바이트 = 약 333자
    'dining_detailinfo': { max: 1333, label: '상세정보' }, // 4000바이트 ÷ 3바이트 = 약 1333자
    'dining_classification': { max: 33, label: '분류' }, // 100바이트 ÷ 3바이트 = 약 33자
    'dining_operation_hours': { max: 333, label: '영업시간' } // 1000바이트 ÷ 3바이트 = 약 333자
  };
  
  for (const [fieldName, validation] of Object.entries(lengthValidations)) {
    const field = document.querySelector(`[name="${fieldName}"]`);
    if (field && field.value && field.value.length > validation.max) {
      errors.push(`${validation.label}은(는) ${validation.max}자 이하여야 합니다. (현재: ${field.value.length}자)`);
    }
  }
  
  // 3. 전화번호 형식 검사
  const phoneField = document.querySelector('[name="phone_number"]');
  if (phoneField && phoneField.value.trim()) {
    const phonePattern = /^[0-9-]+$/;
    if (!phonePattern.test(phoneField.value.trim())) {
      errors.push('문의번호는 숫자와 하이픈(-)만 입력 가능합니다.');
    }
  }
  
  // 4. 예약 가능 여부 검사 (Y/N만 허용)
  const resvField = document.querySelector('[name="dining_resv_availability"]');
  if (resvField && resvField.value && !['Y', 'N'].includes(resvField.value)) {
    errors.push('예약 가능 여부는 Y 또는 N만 입력 가능합니다.');
  }
  
  return errors;
}

// 에러 메시지 표시 함수
function showValidationErrors(errors) {
  if (errors.length === 0) return;
  
  let errorMessage = '다음 오류를 수정해주세요:\n\n';
  errors.forEach((error, index) => {
    errorMessage += `${index + 1}. ${error}\n`;
  });
  
  alert(errorMessage);
}

// 케러셀 이미지 번호 재정렬
function renumberCarouselItems() {
  const carouselList = document.getElementById('carousel-img-list');
  Array.from(carouselList.children).forEach(function(child, i) {
    const span = child.querySelector('span');
    if (span) span.textContent = i + 1;
    child.setAttribute('data-index', i);
  });
}

// 케러셀 이미지 삭제
function deleteCarouselImage(button) {
  const item = button.closest('.carousel-img-item');
  if (!item) return;
  
  const urlInput = item.querySelector('input[name="carousel_image_urls"]');
  if (urlInput && urlInput.value) {
    deletedCarouselUrls.push(urlInput.value); // URL을 직접 저장
    // 디버깅용 로그
    console.log('삭제될 이미지 URL:', urlInput.value);
  }
  item.parentNode.removeChild(item); // 완전히 DOM에서 제거
  renumberCarouselItems();
}

// 케러셀 이미지 교체 트리거
function triggerReplace(idx) {
  let input = document.getElementsByName('replace_carousel_image_' + idx)[0];
  if (!input) {
    input = document.createElement('input');
    input.type = 'file';
    input.name = 'replace_carousel_image_' + idx;
    input.style.display = 'none';
    input.accept = 'image/*';
    input.onchange = function(event) { 
      replaceCarouselImage(event, idx); 
    };
    document.getElementById('diningFileForm').appendChild(input);
  }
  input.click();
}

// 케러셀 이미지 교체
function replaceCarouselImage(event, idx) {
  // 미리보기 변경
  let img = document.querySelectorAll('.carousel-img-item img')[idx];
  img.src = URL.createObjectURL(event.target.files[0]);
  
  // 교체된 파일을 해당 item에 저장
  const items = document.querySelectorAll('.carousel-img-item');
  if (items[idx]) {
    items[idx].file = event.target.files[0]; // 파일 객체 저장
    console.log('교체된 파일 저장:', event.target.files[0].name, '인덱스:', idx);
  }
  
  // 교체 파일 hidden input 추가 (기존 코드 유지)
  let input = document.createElement('input');
  input.type = 'hidden';
  input.name = 'replace_carousel_indexes';
  input.value = idx;
  document.getElementById('diningFileForm').appendChild(input);
}

// 모든 이미지 업로드
function uploadAllImages() {
  console.log('uploadAllImages called!');
  const diningId = document.querySelector('input[name="dining_id"]').value;

  // 메인 이미지
  const mainFile = document.querySelector('input[name="main_image_file"]').files[0];
  if (mainFile) {
    uploadSingleFile(diningId, mainFile, 'main');
  }

  // 로고 이미지
  const logoFile = document.querySelector('input[name="logo_image_file"]').files[0];
  if (logoFile) {
    uploadSingleFile(diningId, logoFile, 'logo');
  }

  // 케러셀 이미지(여러 장)
  const carouselFiles = document.querySelector('input[name="carousel_image_files"]').files;
  for (let i = 0; i < carouselFiles.length; i++) {
    uploadSingleFile(diningId, carouselFiles[i], 'carousel', i);
  }
}

// 단일 파일 업로드
function uploadSingleFile(diningId, file, type, idx) {
  const formData = new FormData();
  formData.append('dining_id', diningId);
  if (type === 'main') {
    formData.append('main_image_file', file);
  } else if (type === 'logo') {
    formData.append('logo_image_file', file);
  } else if (type === 'carousel') {
    formData.append('carousel_image_file', file);
    formData.append('carousel_index', idx);
  }
  return fetch('/admin/dining/editFiles', {
    method: 'POST',
    body: formData
  })
  .then(res => {
    if (!res.ok) throw new Error('업로드 실패');
    return res.json();
  })
  .then(data => {
    // 업로드 성공 시 미리보기 변경
    if (type === 'main') {
      // 메인 이미지 미리보기 변경
      const mainImg = document.querySelector('input[name="main_image_file"]').nextElementSibling;
      if (mainImg && mainImg.tagName === 'IMG') {
        mainImg.src = data.imageUrl + '?t=' + new Date().getTime();
      }
    } else if (type === 'logo') {
      // 로고 이미지 미리보기 변경
      const logoImg = document.querySelector('input[name="logo_image_file"]').nextElementSibling;
      if (logoImg && logoImg.tagName === 'IMG') {
        logoImg.src = data.imageUrl + '?t=' + new Date().getTime();
      }
    } else if (type === 'carousel') {
      // idx번째 케러셀 이미지 미리보기 변경
      const carouselImgs = document.querySelectorAll('.carousel-img-item img');
      if (carouselImgs[idx]) {
        carouselImgs[idx].src = data.imageUrl + '?t=' + new Date().getTime();
      }
    }
  });
}

// 케러셀 이미지 저장
function saveCarouselImages() {
  const diningId = document.querySelector('input[name="dining_id"]').value;
  const items = document.querySelectorAll('.carousel-img-item');
  const uploadPromises = [];

  console.log('=== 케러셀 이미지 저장 시작 ===');
  console.log('총 케러셀 아이템 수:', items.length);
  console.log('메인 이미지 파일:', mainImageFile ? mainImageFile.name : '없음');
  console.log('로고 이미지 파일:', logoImageFile ? logoImageFile.name : '없음');

  // 메인 이미지 업로드
  if (mainImageFile) {
    const formData = new FormData();
    formData.append('dining_id', diningId);
    formData.append('main_image_file', mainImageFile);
    uploadPromises.push(
      fetch('/admin/dining/editFiles', {
        method: 'POST',
        body: formData
      })
      .then(res => res.json())
      .then(data => {
        document.querySelector('input[name="main_image_url"]').value = data.imageUrl;
      })
    );
  }

  // 로고 이미지 업로드
  if (logoImageFile) {
    const formData = new FormData();
    formData.append('dining_id', diningId);
    formData.append('logo_image_file', logoImageFile);
    uploadPromises.push(
      fetch('/admin/dining/editFiles', {
        method: 'POST',
        body: formData
      })
      .then(res => res.json())
      .then(data => {
        document.querySelector('input[name="logo_image_url"]').value = data.imageUrl;
      })
    );
  }

  // 케러셀 이미지 업로드 (추가/교체된 것만)
  items.forEach((item, idx) => {
    const file = item.file;
    console.log(`케러셀 아이템 ${idx + 1}:`, {
      hasFile: !!file,
      fileName: file ? file.name : '없음',
      hasUrl: !!item.querySelector('input[name="carousel_image_urls"]')?.value
    });
    
    if (file) {
      console.log(`케러셀 이미지 업로드 시작: ${file.name}, 인덱스: ${idx + 1}`);
      const formData = new FormData();
      formData.append('dining_id', diningId);
      formData.append('carousel_image_file', file);
      formData.append('carousel_index', idx + 1);
      uploadPromises.push(
        fetch('/admin/dining/editFiles', {
          method: 'POST',
          body: formData
        })
        .then(res => res.json())
        .then(data => {
          console.log(`케러셀 이미지 업로드 성공: ${data.imageUrl}`);
          const urlInput = item.querySelector('input[name="carousel_image_urls"]');
          if (urlInput) urlInput.value = data.imageUrl;
        })
      );
    }
  });

  // 모든 업로드가 끝난 후 순서 동기화
  Promise.all(uploadPromises).then(() => {
    const orderedUrls = [];
    items.forEach(item => {
      const urlInput = item.querySelector('input[name="carousel_image_urls"]');
      if (urlInput && urlInput.value) {
        orderedUrls.push(urlInput.value);
      }
    });
    updateCarouselOrderOnServer(diningId, orderedUrls);
    // 업로드 후 파일 변수 초기화
    mainImageFile = null;
    logoImageFile = null;
    items.forEach(item => { item.file = null; });
  }).catch(() => {
    alert('업로드 실패');
  });
}

// 서버에 케러셀 순서 업데이트
function updateCarouselOrderOnServer(diningId, imageUrls) {
  console.log('=== 케러셀 순서 업데이트 ===');
  console.log('다이닝 ID:', diningId);
  console.log('이미지 URLs:', imageUrls);
  console.log('삭제될 이미지 URL 전체:', deletedCarouselUrls);

  const bodyData = `dining_id=${encodeURIComponent(diningId)}&` +
    imageUrls.map(url => `image_urls=${encodeURIComponent(url)}`).join('&') +
    deletedCarouselUrls.map(url => `&deleted_image_urls=${encodeURIComponent(url)}`).join('');
  console.log('fetch body:', bodyData); // 실제 전송되는 데이터 확인
  fetch('/admin/dining/updateCarouselOrder', {
    method: 'POST',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    body: bodyData
  })
  .then(res => {
    if (res.ok) {
      alert('저장 완료!');
      location.reload();
    } else {
      alert('순서 저장 실패');
    }
  })
  .catch(error => {
    console.error('Error updating carousel order:', error);
    alert('순서 저장 실패');
  });
}

// 단일 파일 업로드 및 hidden 업데이트
function uploadSingleFileAndUpdateHidden(diningId, file, idx, type, div) {
  const formData = new FormData();
  formData.append('dining_id', diningId);
  if (type === 'main') {
    formData.append('main_image_file', file);
  } else if (type === 'logo') {
    formData.append('logo_image_file', file);
  } else {
    formData.append('carousel_image_file', file);
    formData.append('carousel_index', idx + 1);
  }
  fetch('/admin/dining/editFiles', {
    method: 'POST',
    body: formData
  })
  .then(res => res.json())
  .then(data => {
    // 업로드 후 해당 .carousel-img-item의 hidden input value를 새 URL로 갱신
    const urlInput = div.querySelector('input[name="carousel_image_urls"]');
    if (urlInput) urlInput.value = data.imageUrl;
  });
}

// 케러셀 이미지 추가
function addCarouselImage() {
  const carouselList = document.getElementById('carousel-img-list');
  const idx = carouselList.children.length;
  const div = document.createElement('div');
  div.className = 'carousel-img-item';
  div.style.position = 'relative';
  div.style.display = 'flex';
  div.style.alignItems = 'center';
  div.style.padding = '12px 0';
  div.style.borderBottom = '1px solid #e0e0e0';
  div.setAttribute('data-index', idx);
  
  // 번호(순번)
  const numSpan = document.createElement('span');
  numSpan.style.width = '32px';
  numSpan.style.display = 'inline-block';
  numSpan.style.textAlign = 'center';
  numSpan.style.fontWeight = 'bold';
  numSpan.textContent = idx + 1;
  div.appendChild(numSpan);
  
  // 이미지 미리보기
  const img = document.createElement('img');
  img.style.maxWidth = '120px';
  img.style.border = '1px solid #ccc';
  img.style.borderRadius = '4px';
  img.style.margin = '0 16px';
  div.appendChild(img);
  
  // 파일 input
  const fileInput = document.createElement('input');
  fileInput.type = 'file';
  fileInput.accept = 'image/*';
  fileInput.onchange = function(e) {
    if (e.target.files[0]) {
      img.src = URL.createObjectURL(e.target.files[0]);
      div.file = e.target.files[0]; // 파일 객체 저장
      hiddenInput.value = '';
    }
  };
  div.appendChild(fileInput);
  
  // hidden input (업로드 후 서버에서 반환된 URL로 갱신)
  const hiddenInput = document.createElement('input');
  hiddenInput.type = 'hidden';
  hiddenInput.name = 'carousel_image_urls';
  div.appendChild(hiddenInput);
  
  // 삭제 버튼
  const delBtn = document.createElement('button');
  delBtn.type = 'button';
  delBtn.textContent = '삭제';
  delBtn.className = 'action-btn delete-btn';
  delBtn.onclick = function() { deleteCarouselImage(this); };
  div.appendChild(delBtn);
  
  // 교체 버튼
  const replaceBtn = document.createElement('button');
  replaceBtn.type = 'button';
  replaceBtn.textContent = '교체';
  replaceBtn.className = 'action-btn replace-btn';
  replaceBtn.onclick = function() { fileInput.click(); };
  div.appendChild(replaceBtn);
  
  // 추가
  carouselList.appendChild(div);
  renumberCarouselItems();
}

// 페이지 로드 시 이벤트 리스너 등록
document.addEventListener('DOMContentLoaded', function() {
  // 데이터 수정 버튼 클릭 이벤트
  const submitBtn = document.getElementById('submit-btn');
  if (submitBtn) {
    submitBtn.addEventListener('click', function(e) {
      e.preventDefault(); // 기본 동작 막기
      
      // 유효성 검사 수행
      const validationErrors = validateDiningForm();
      if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return; // 유효성 검사 실패 시 현재 페이지에 머무름
      }
      
      // 유효성 검사 통과 시 기존 폼 제출
      const diningDataForm = document.getElementById('diningDataForm');
      if (diningDataForm) {
        diningDataForm.submit();
      }
    });
  }

  // 케러셀 이미지 추가 버튼 추가
  const addBtn = document.createElement('button');
  addBtn.type = 'button';
  addBtn.textContent = '케러셀 이미지 추가';
  addBtn.onclick = addCarouselImage;
  const carouselSection = document.getElementById('carousel-img-list');
  if (carouselSection) {
    carouselSection.parentNode.insertBefore(addBtn, carouselSection);
  }

  // 파일 폼 제출 이벤트
  const diningFileForm = document.getElementById('diningFileForm');
  if (diningFileForm) {
    diningFileForm.addEventListener('submit', function(e) {
      // input[file] 개수 확인
      const files = this.querySelectorAll('input[type="file"]');
      console.log('input[file] 개수:', files.length);
      files.forEach((input, idx) => {
        console.log(idx, input.name, input.files.length, input.files[0]?.name);
      });

      // input[type=hidden]도 확인
      const hiddens = this.querySelectorAll('input[type="hidden"]');
      hiddens.forEach((input, idx) => {
        console.log('hidden', idx, input.name, input.value);
      });
    });
  }

  // 메인 이미지 파일 선택 시 미리보기만 갱신, 업로드는 저장 시에만
  const mainInput = document.querySelector('input[name="main_image_file"]');
  if (mainInput) {
    mainInput.addEventListener('change', function(e) {
      const file = e.target.files[0];
      if (file) {
        mainImageFile = file;
        // 미리보기 이미지 찾기 (input 다음에 있는 img 태그)
        const img = this.parentNode.parentNode.querySelector('img');
        if (img) {
          img.src = URL.createObjectURL(file);
          img.style.display = 'block';
        }
      }
    });
  }

  // 로고 이미지 파일 선택 시 미리보기만 갱신, 업로드는 저장 시에만
  const logoInput = document.querySelector('input[name="logo_image_file"]');
  if (logoInput) {
    logoInput.addEventListener('change', function(e) {
      const file = e.target.files[0];
      if (file) {
        logoImageFile = file;
        // 미리보기 이미지 찾기 (input 다음에 있는 img 태그)
        const img = this.parentNode.parentNode.querySelector('img');
        if (img) {
          img.src = URL.createObjectURL(file);
          img.style.display = 'block';
        }
      }
    });
  }

  // 케러셀 이미지 여러 장 선택 시 미리보기 즉시 변경 (추가된 파일만 미리보기)
  const carouselInput = document.querySelector('input[name="carousel_image_files"]');
  if (carouselInput) {
    carouselInput.addEventListener('change', function(e) {
      const files = e.target.files;
      const carouselList = document.getElementById('carousel-img-list');
      // 기존 미리보기는 그대로 두고, 새로 선택한 파일만 추가 미리보기로 보여줌
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        // 이미 있는 미리보기는 건너뜀
        if (carouselList.children[i]) {
          const img = carouselList.children[i].querySelector('img');
          if (img) {
            img.src = URL.createObjectURL(file);
          }
        }
      }
    });
  }
}); 