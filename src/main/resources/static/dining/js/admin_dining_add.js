// 다이닝 추가 페이지 JavaScript

// 삭제된 케러셀 이미지 URL들을 저장할 배열
let deletedCarouselUrls = [];

// 파일 업로드용 변수 선언
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

// 실시간 유효성 검사 함수들
function validateField(fieldName, value) {
  const errors = [];
  
  // 필수 필드 검사
  const requiredFields = ['dining_name', 'type', 'location', 'phone_number', 'manager_name', 'dining_classification'];
  if (requiredFields.includes(fieldName) && (!value || !value.trim())) {
    errors.push('필수 입력 항목입니다.');
    return errors;
  }
  
  // 길이 검사 (한글 기준으로 조정)
  const lengthValidations = {
    'dining_name': 66, // 200바이트 ÷ 3바이트 = 약 66자
    'type': 33, // 100바이트 ÷ 3바이트 = 약 33자
    'location': 6, // 20바이트 ÷ 3바이트 = 약 6자
    'phone_number': 33, // 100바이트 ÷ 3바이트 = 약 33자
    'manager_name': 5, // 15바이트 ÷ 3바이트 = 5자
    'dining_introduction': 333, // 1000바이트 ÷ 3바이트 = 약 333자
    'dining_detailinfo': 1333, // 4000바이트 ÷ 3바이트 = 약 1333자
    'dining_classification': 33, // 100바이트 ÷ 3바이트 = 약 33자
    'dining_operation_hours': 333 // 1000바이트 ÷ 3바이트 = 약 333자
  };
  
  if (lengthValidations[fieldName] && value && value.length > lengthValidations[fieldName]) {
    errors.push(`${lengthValidations[fieldName]}자 이하여야 합니다. (현재: ${value.length}자)`);
  }
  
  // 전화번호 형식 검사
  if (fieldName === 'phone_number' && value && value.trim()) {
    const phonePattern = /^[0-9-]+$/;
    if (!phonePattern.test(value.trim())) {
      errors.push('숫자와 하이픈(-)만 입력 가능합니다.');
    }
  }
  
  // 예약 가능 여부 검사
  if (fieldName === 'dining_resv_availability' && value && !['Y', 'N'].includes(value)) {
    errors.push('Y 또는 N만 입력 가능합니다.');
  }
  
  return errors;
}

// 필드 유효성 상태 업데이트
function updateFieldValidation(fieldName, value) {
  const field = document.querySelector(`[name="${fieldName}"]`);
  const errorElement = document.getElementById(`${fieldName}_error`);
  
  if (!field || !errorElement) return;
  
  const errors = validateField(fieldName, value);
  
  // 에러 메시지 표시/숨김
  if (errors.length > 0) {
    errorElement.textContent = errors[0];
    errorElement.classList.add('show');
    field.classList.remove('valid');
    field.classList.add('error');
  } else {
    errorElement.textContent = '';
    errorElement.classList.remove('show');
    field.classList.remove('error');
    if (value && value.trim()) {
      field.classList.add('valid');
    } else {
      field.classList.remove('valid');
    }
  }
}

// saveCarouselImages 함수 콜백 지원, diningId 명시적으로 받음
function saveCarouselImages(diningId, callback) {
  const items = document.querySelectorAll('.carousel-img-item');
  const uploadPromises = [];

  // 파일 선택 여부 확인
  const hasMainFile = mainImageFile !== null;
  const hasLogoFile = logoImageFile !== null;
  const hasCarouselFiles = Array.from(items).some(item => item.file !== null);
  
  console.log('파일 선택 상태:', {
    mainFile: hasMainFile,
    logoFile: hasLogoFile,
    carouselFiles: hasCarouselFiles,
    totalItems: items.length
  });

  // 업로드할 파일이 없으면 바로 성공 처리
  if (!hasMainFile && !hasLogoFile && !hasCarouselFiles) {
    console.log('업로드할 파일이 없습니다.');
    if (callback) callback();
    return;
  }

  // 메인 이미지 업로드
  if (mainImageFile) {
    console.log('메인 이미지 업로드 시작:', mainImageFile.name);
    const formData = new FormData();
    formData.append('dining_id', diningId);
    formData.append('main_image_file', mainImageFile);
    uploadPromises.push(
      fetch('/admin/dining/editFiles', {
        method: 'POST',
        body: formData
      })
      .then(res => {
        if (!res.ok) {
          return res.text().then(text => {
            console.error('메인 이미지 업로드 실패:', res.status, res.statusText, text);
            throw new Error('메인 이미지 업로드 실패: ' + text);
          });
        }
        return res.json();
      })
      .then(data => {
        console.log('메인 이미지 업로드 성공:', data.imageUrl);
        document.querySelector('input[name="main_image_url"]').value = data.imageUrl;
      })
    );
  }

  // 로고 이미지 업로드
  if (logoImageFile) {
    console.log('로고 이미지 업로드 시작:', logoImageFile.name);
    const formData = new FormData();
    formData.append('dining_id', diningId);
    formData.append('logo_image_file', logoImageFile);
    uploadPromises.push(
      fetch('/admin/dining/editFiles', {
        method: 'POST',
        body: formData
      })
      .then(res => {
        if (!res.ok) {
          return res.text().then(text => {
            console.error('로고 이미지 업로드 실패:', res.status, res.statusText, text);
            throw new Error('로고 이미지 업로드 실패: ' + text);
          });
        }
        return res.json();
      })
      .then(data => {
        console.log('로고 이미지 업로드 성공:', data.imageUrl);
        document.querySelector('input[name="logo_image_url"]').value = data.imageUrl;
      })
    );
  }

  // 케러셀 이미지 업로드 (추가/교체된 것만)
  items.forEach((item, idx) => {
    const file = item.file;
    if (file) {
      console.log('케러셀 이미지 업로드 시작:', file.name, '인덱스:', idx + 1);
      const formData = new FormData();
      formData.append('dining_id', diningId);
      formData.append('carousel_image_file', file);
      formData.append('carousel_index', idx + 1);
      uploadPromises.push(
        fetch('/admin/dining/editFiles', {
          method: 'POST',
          body: formData
        })
        .then(res => {
          if (!res.ok) {
            return res.text().then(text => {
              console.error('케러셀 업로드 실패:', res.status, res.statusText, text);
              throw new Error('케러셀 업로드 실패: ' + text);
            });
          }
          return res.json();
        })
        .then(data => {
          console.log('케러셀 이미지 업로드 성공:', data.imageUrl);
          const urlInput = item.querySelector('input[name="carousel_image_urls"]');
          if (urlInput) urlInput.value = data.imageUrl;
        })
      );
    }
  });

  // 모든 업로드가 끝난 후 순서 동기화
  Promise.all(uploadPromises).then(() => {
    console.log('모든 파일 업로드 완료');
    const orderedUrls = [];
    items.forEach(item => {
      const urlInput = item.querySelector('input[name="carousel_image_urls"]');
      if (urlInput && urlInput.value) {
        orderedUrls.push(urlInput.value);
      }
    });
    updateCarouselOrderOnServer(diningId, orderedUrls, () => {
      if (callback) callback();
    });
    mainImageFile = null;
    logoImageFile = null;
    items.forEach(item => { item.file = null; });
  }).catch((error) => {
    console.error('업로드 실패:', error);
    alert('업로드 실패: ' + error.message);
  });
}

// updateCarouselOrderOnServer 콜백 지원
function updateCarouselOrderOnServer(diningId, imageUrls, callback) {
  let bodyData = `dining_id=${encodeURIComponent(diningId)}`;
  
  // 이미지 URL들 추가
  if (imageUrls && imageUrls.length > 0) {
    bodyData += '&' + imageUrls.map(url => `image_urls=${encodeURIComponent(url)}`).join('&');
  }
  
  // 삭제된 이미지 URL들 추가
  if (deletedCarouselUrls && deletedCarouselUrls.length > 0) {
    bodyData += '&' + deletedCarouselUrls.map(url => `deleted_image_urls=${encodeURIComponent(url)}`).join('&');
  }
  
  console.log('케러셀 순서 업데이트 요청:', bodyData);
  
  fetch('/admin/dining/updateCarouselOrder', {
    method: 'POST',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    body: bodyData
  })
  .then(res => {
    if (res.ok) {
      console.log('케러셀 순서 업데이트 성공');
      if (callback) callback();
    } else {
      console.error('케러셀 순서 업데이트 실패:', res.status, res.statusText);
      alert('순서 저장 실패');
    }
  })
  .catch(error => {
    console.error('케러셀 순서 업데이트 에러:', error);
    alert('순서 저장 실패');
  });
}

// 케러셀 이미지 추가 함수
function addCarouselImageWithFile(file) {
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
  img.src = URL.createObjectURL(file);
  div.appendChild(img);
  
  // 파일 input (hidden, 교체용)
  const fileInput = document.createElement('input');
  fileInput.type = 'file';
  fileInput.accept = 'image/*';
  fileInput.style.display = 'none';
  fileInput.onchange = function(e) {
    if (e.target.files[0]) {
      img.src = URL.createObjectURL(e.target.files[0]);
      div.file = e.target.files[0];
      hiddenInput.value = '';
    }
  };
  div.appendChild(fileInput);
  
  // hidden input (업로드 후 서버에서 반환된 URL로 갱신)
  const hiddenInput = document.createElement('input');
  hiddenInput.type = 'hidden';
  hiddenInput.name = 'carousel_image_urls';
  div.appendChild(hiddenInput);
  
  // 파일 객체 저장
  div.file = file;
  
  // 삭제 버튼
  const delBtn = document.createElement('button');
  delBtn.type = 'button';
  delBtn.textContent = '삭제';
  delBtn.className = 'action-btn delete-btn';
  delBtn.onclick = function() { 
    div.parentNode.removeChild(div); 
    renumberCarouselItems(); 
  };
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

// 케러셀 이미지 번호 재정렬
function renumberCarouselItems() {
  const items = document.querySelectorAll('.carousel-img-item');
  items.forEach((item, index) => {
    const numSpan = item.querySelector('span');
    if (numSpan) {
      numSpan.textContent = index + 1;
    }
  });
}

// 페이지 로드 시 이벤트 리스너 등록
document.addEventListener('DOMContentLoaded', function() {
  // 모든 입력 필드에 실시간 유효성 검사 이벤트 리스너 추가
  const fields = ['dining_name', 'type', 'location', 'phone_number', 'manager_name', 
                  'dining_resv_availability', 'dining_classification', 'dining_introduction', 
                  'dining_detailinfo', 'dining_operation_hours'];
  
  fields.forEach(fieldName => {
    const field = document.querySelector(`[name="${fieldName}"]`);
    if (field) {
      // blur 이벤트 (포커스가 벗어날 때)
      field.addEventListener('blur', function() {
        updateFieldValidation(fieldName, this.value);
      });
      
      // input 이벤트 (입력할 때마다)
      field.addEventListener('input', function() {
        updateFieldValidation(fieldName, this.value);
      });
    }
  });

  // '추가' 버튼 클릭 시 다이닝 정보 + 이미지 + 케러셀 저장이 한 번에 일어나도록 처리
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
      
      // diningDataForm의 데이터 가져오기
      const diningDataForm = document.getElementById('diningDataForm');
      const formData = new FormData(diningDataForm);
      
      fetch('/admin/dining/add', {
        method: 'POST',
        body: formData
      })
      .then(res => res.json())
      .then(data => {
        const diningId = data.dining_id;
        // diningFileForm 내의 input[name="dining_id"]에 세팅
        const fileFormDiningId = document.querySelector('#diningFileForm input[name="dining_id"]');
        if (fileFormDiningId) fileFormDiningId.value = diningId;
        saveCarouselImages(diningId, () => {
          window.location.href = '/admin/dining';
        });
      })
      .catch(error => {
        console.error('다이닝 정보 저장 실패:', error);
        alert('다이닝 정보 저장에 실패했습니다.');
      });
    });
  }

  // '케러셀 이미지 추가' 버튼 클릭 시 파일 선택창 열기
  const addCarouselBtn = document.getElementById('add-carousel-btn');
  if (addCarouselBtn) {
    addCarouselBtn.addEventListener('click', function() {
      document.getElementById('add-carousel-images').click();
    });
  }

  // 여러 장 한 번에 추가 (input[type=file][multiple])
  const addCarouselImages = document.getElementById('add-carousel-images');
  if (addCarouselImages) {
    addCarouselImages.addEventListener('change', function(e) {
      const files = Array.from(e.target.files);
      files.forEach(file => {
        addCarouselImageWithFile(file);
      });
      // 파일 선택창 초기화 (같은 파일 다시 선택 가능하게)
      e.target.value = '';
    });
  }

  // 메인 이미지 파일 선택 시 미리보기
  const mainImageInput = document.querySelector('input[name="main_image_file"]');
  if (mainImageInput) {
    mainImageInput.addEventListener('change', function(e) {
      mainImageFile = e.target.files[0];
      const preview = document.getElementById('main-image-preview');
      if (mainImageFile) {
        preview.src = URL.createObjectURL(mainImageFile);
        preview.style.display = 'block';
      } else {
        preview.style.display = 'none';
      }
    });
  }

  // 로고 이미지 파일 선택 시 미리보기
  const logoImageInput = document.querySelector('input[name="logo_image_file"]');
  if (logoImageInput) {
    logoImageInput.addEventListener('change', function(e) {
      logoImageFile = e.target.files[0];
      const preview = document.getElementById('logo-image-preview');
      if (logoImageFile) {
        preview.src = URL.createObjectURL(logoImageFile);
        preview.style.display = 'block';
      } else {
        preview.style.display = 'none';
      }
    });
  }

  // 파일 폼 제출 이벤트
  const diningFileForm = document.getElementById('diningFileForm');
  if (diningFileForm) {
    diningFileForm.addEventListener('submit', function(e) {
      e.preventDefault();

      // 1. 다이닝 정보 먼저 저장
      const diningDataForm = document.getElementById('diningDataForm');
      const formData = new FormData(diningDataForm);

      fetch('/admin/dining/add', {
        method: 'POST',
        body: formData
      })
      .then(res => res.json())
      .then(data => {
        const diningId = data.dining_id;
        // hidden input에 세팅
        const fileFormDiningId = document.querySelector('#diningFileForm input[name="dining_id"]');
        if (fileFormDiningId) fileFormDiningId.value = diningId;
        // 2. 이미지 업로드
        saveCarouselImages(diningId, () => {
          window.location.href = '/admin/dining';
        });
      });
    });
  }
}); 