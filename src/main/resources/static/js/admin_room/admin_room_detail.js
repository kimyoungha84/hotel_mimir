$(function() {
  $('#thumbnailImage').on('click', function() {
    $('#fileInput').click();
  });
  
  $('#fileInput').on('change', function (event) {
    const file = event.target.files[0];
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = function (e) {
        $('#thumbnailImage').attr('src', e.target.result);
      };
      reader.readAsDataURL(file);
    }
  });
  
});