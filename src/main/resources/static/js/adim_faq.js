document.addEventListener("DOMContentLoaded", () => {
  const faqList = document.getElementById("faqList");
  const selectAll = document.getElementById("selectAll");

  // 샘플 FAQ 데이터
/*  const faqData = [
    { id: 1, title: "고객 정보를 잃어버렸는데 수정 가능한가요?", date: "2025-06-20" },
    { id: 2, title: "문의한 내용은 어떻게 확인할 수 있나요?", date: "2025-06-19" },
    { id: 3, title: "공식적인 이용에 대해 알려주세요.", date: "2025-06-18" },
    { id: 4, title: "로그인이 되지 않습니다.", date: "2025-06-17" }
  ];*/

  // FAQ 리스트 그리기
  function renderFAQ() {
    faqList.innerHTML = "";
    faqData.forEach((faq, index) => {
      const row = `
        <tr>
          <td><input type="checkbox" class="rowCheck" value="${faq.num}" /></td>
          <td>${faq.num}</td>
          <td>${faq.title}</td>
          <td>${faq.date}</td>
        </tr>
      `;
      faqList.insertAdjacentHTML("beforeend", row);
    });
  }

  // 전체 선택
  selectAll.addEventListener("change", () => {
    document.querySelectorAll(".rowCheck").forEach(chk => {
      chk.checked = selectAll.checked;
    });
  });

  renderFAQ();
});
$(document).ready(function() {
  $(".btn apply").click(function() {
    window.location.href = "/admin_faq_register";
  });
});

//faq 삭제 event
document.addEventListener("DOMContentLoaded", function () {
  const deleteBtn = document.querySelector(".btn.delete");

  deleteBtn.addEventListener("click", function () {
    const checked = document.querySelectorAll("input[name='chk']:checked");
    if (checked.length === 0) {
      alert("삭제할 항목을 선택하세요.");
      return;
    }

    if (!confirm("정말 삭제하시겠습니까?")) return;

    const ids = Array.from(checked).map(cb => cb.value);

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
        // 화면에서 행 제거
        checked.forEach(cb => cb.closest("tr").remove());
        alert("삭제되었습니다.");
      })
      .catch(err => {
        console.error(err);
        alert("삭제 중 오류 발생");
      });
  });
});

