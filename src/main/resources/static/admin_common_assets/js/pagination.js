/**
 * 
 */
// static/js/pagination.js
window.loadPage = function (pageNum) {
  if (!(window.lastSearchParams instanceof URLSearchParams)) {
    console.warn("lastSearchParams가 정의되어 있지 않습니다.");
    return;
  }

  const baseParams = new URLSearchParams(window.lastSearchParams.toString());

  const pageSize = parseInt(baseParams.get("pageSize")) || 10;
  const offset = (pageNum - 1) * pageSize + 1; // 시작 row 번호 (1-based)
  const end = offset + pageSize - 1;              // 끝 row 번호

  baseParams.set("offset", offset);
  baseParams.set("end", end);
  baseParams.set("currentPage", pageNum);

  fetch('/count?' + baseParams.toString())
    .then(res => res.json())
    .then(json => {
      baseParams.set("totalItems", json.totalItems);

      fetch('/search?' + baseParams.toString())
        .then(res => res.text())
        .then(html => {
          document.getElementById("resultArea").innerHTML = html;

          fetch('/reBuildPagination?' + baseParams.toString())
            .then(res => res.text())
            .then(paginationHtml => {
              document.getElementById("paginationArea").innerHTML = paginationHtml;
            });
        });
    });
};
