$(function () {
  $('#thumbnailImage').on('click', function () {
    $('#fileInput').click();
  });

  $('#fileInput').on('change', function (event) {
    const file = event.target.files[0];
    const maxSizeMB = 10;

    if (!file) return;

    // 이미지 파일인지 확인
    const validTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
    if (!validTypes.includes(file.type)) {
      alert('JPG, PNG, GIF, WEBP 형식의 이미지만 업로드 가능합니다.');
      return;
    }

    // 용량 제한
    if (file.size > maxSizeMB * 1024 * 1024) {
      alert(`${maxSizeMB}MB 이하의 이미지만 업로드 가능합니다.`);
      return;
    }

    // ✅ 안정적인 미리보기 (base64 대신 Object URL)
    const objectURL = URL.createObjectURL(file);
    $('#thumbnailImage').attr('src', objectURL);

    // ⚠️ 필요 시: 메모리 해제 (이미지 로드 후)
    $('#thumbnailImage').on('load', function () {
      URL.revokeObjectURL(objectURL); // 사용 후 메모리 해제
    });
  });
});
