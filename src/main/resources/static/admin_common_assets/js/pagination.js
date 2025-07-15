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

    // filterType만 사용, fragment 관련 변수 제거
    // var filterType = window.filterType || 'defaultFilterType';
    // var filterFragmentTemplate = window.filterFragmentTemplate || 'defaultTemplate';
    // var filterFragmentName = window.filterFragmentName || 'defaultFragment';
    // var filterResultKey = window.filterResultKey || 'defaultResultKey';

    // URLSearchParams 사용
    // baseParams.set("filterType", filterType); // 이미 포함되어 있음
    // fragment 관련 파라미터 세팅 제거

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
    fetch('/count', {
        method: 'POST',
        body: baseParams
    })
        .then(res => res.json())
        .then(json => {
            baseParams.set("totalItems", json.totalItems);

            // search 요청
            fetch('/search', {
                method: 'POST',
                body: baseParams
            })
                .then(res => res.text())
                .then(html => {
                    document.getElementById("resultArea").innerHTML = html;

                    // pagination 재빌드
                    fetch('/reBuildPagination', {
                        method: 'POST',
                        body: baseParams
                    })
                        .then(res => res.text())
                        .then(paginationHtml => {
                            const pgArea = document.getElementById("paginationArea");
                            if (pgArea) {
                                pgArea.outerHTML = paginationHtml;
                            }
                        });
                });
        });
};

// 최초 페이지 로딩 후 loadPage 함수 호출
document.addEventListener('DOMContentLoaded', function() {
    var config = document.getElementById("pagination-config");
    var params = new URLSearchParams();
    if (config) {
        var pageSize = config.querySelector('input[name="pageSize"]').value;
        var filterTypeInput = config.querySelector('input[name="filterType"]');
        if (filterTypeInput) {
            params.set("filterType", filterTypeInput.value);
        }
        params.set("pageSize", pageSize);
    }
    window.lastSearchParams = params;
    window.loadPage(1);
});
