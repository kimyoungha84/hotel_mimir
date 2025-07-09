// window.loadPage 함수는 DOMContentLoaded 외부에서 미리 정의
window.loadPage = function (pageNum) {
    let baseParams;

    // window.lastSearchParams가 정의되지 않은 경우 처리
    if (!(window.lastSearchParams instanceof URLSearchParams)) {
        baseParams = new URLSearchParams();
        console.warn("lastSearchParams가 정의되어 있지 않습니다.");
    } else {
        baseParams = new URLSearchParams(window.lastSearchParams.toString());
    }

    const config = document.getElementById("pagination-config");

    var filterType = window.filterType || 'defaultFilterType';
    var filterFragmentTemplate = window.filterFragmentTemplate || 'defaultTemplate';
    var filterFragmentName = window.filterFragmentName || 'defaultFragment';
    var filterResultKey = window.filterResultKey || 'defaultResultKey';

    console.log('filterType:', filterType);
    console.log('filterFragmentTemplate:', filterFragmentTemplate);
    console.log('filterFragmentName:', filterFragmentName);
    console.log('filterResultKey:', filterResultKey);

    // URLSearchParams 사용
    baseParams.set("filterType", filterType);
    baseParams.set("filter_fragmentTemplate", filterFragmentTemplate);
    baseParams.set("filter_fragmentName", filterFragmentName);
    baseParams.set("filter_resultKey", filterResultKey);

    console.log(baseParams.toString());

    // 페이지네이션 세팅
    const pageSize = config.querySelector('input[name="pageSize"]').value;
    const offset = (pageNum - 1) * pageSize + 1; // 시작 row 번호 (1-based)
    const end = offset + pageSize - 1;           // 끝 row 번호

    // 파라미터 세팅
    baseParams.set("offset", offset);
    baseParams.set("end", end);
    baseParams.set("currentPage", pageNum);
    baseParams.set("pageSize", pageSize);

    // count 요청
    fetch('/count?' + baseParams.toString())
        .then(res => res.json())
        .then(json => {
            baseParams.set("totalItems", json.totalItems);

            // search 요청
            fetch('/search?' + baseParams.toString())
                .then(res => res.text())
                .then(html => {
                    document.getElementById("resultArea").innerHTML = html;

                    // pagination 재빌드
                    fetch('/reBuildPagination?' + baseParams.toString())
                        .then(res => res.text())
                        .then(paginationHtml => {
                            document.getElementById("paginationArea").innerHTML = paginationHtml;
                        });
                });
        });
};

// 최초 페이지 로딩 후 loadPage 함수 호출
document.addEventListener('DOMContentLoaded', function() {
    window.loadPage(1); // 최초 1회 페이지 호출
});
