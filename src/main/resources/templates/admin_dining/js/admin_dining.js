// 페이지 로딩 완료 후 실행
document.addEventListener("DOMContentLoaded", function () {
  // ✅ 전체 선택 체크박스
  const selectAll = document.getElementById("selectAll");

  // 전체 선택 이벤트
  selectAll.addEventListener("change", function () {
    document.querySelectorAll("input[name='chk']").forEach(chk => {
      chk.checked = selectAll.checked;
    });
  });

  // ✅ 삭제 버튼
  const deleteBtn = document.getElementById("deleteBtn");

  deleteBtn.addEventListener("click", function () {
    const checked = document.querySelectorAll("input[name='chk']:checked");

    if (checked.length === 0) {
      alert("삭제할 항목을 선택하세요.");
      return;
    }

    const ids = Array.from(checked).map(cb => cb.value);
    console.log("🔧 삭제할 ID 목록:", ids);

    fetch("/admin/faq/delete", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(ids),
    })
      .then(res => {
        if (!res.ok) throw new Error("삭제 실패");
        return res.text();
      })
      .then(() => {
        checked.forEach(cb => cb.closest("tr").remove());
        alert("삭제되었습니다.");
      })
      .catch(err => {
        console.error("❌ 삭제 중 오류:", err);
        alert("삭제 중 오류 발생");
      });
  });

  // ✅ 등록 버튼 (FAQ 등록 페이지로 이동)
  const registerBtn = document.querySelector(".btn.apply");
  if (registerBtn) {
    registerBtn.addEventListener("click", function () {
      window.location.href = "/admin/faq/register";
    });
  }
  // ✅ 수정 버튼 클릭 시
/*  const modifyBtn = document.getElementById("modifyBtn");
  if (modifyBtn) {
    modifyBtn.addEventListener("click", function () {
      const checked = document.querySelectorAll("input[name='chk']:checked");
      if (checked.length !== 1) {
        alert("수정할 항목을 하나만 선택하세요.");
        return;
      }

      const faqId = checked[0].value;
      window.location.href = `/admin/faq/modify?num=${faqId}`;
    });
  }*/
  $(document).ready(function () {
    $("#btnModify").click(function () {
      const selected = $("input[name=chk]:checked");
      if (selected.length !== 1) {
        alert("수정할 항목 하나만 선택하세요.");
        return;
      }
      const faqNum = selected.val();
      location.href = "/admin/faq/modify?faq_num=" + faqNum;
    });
  });

  
  
});
