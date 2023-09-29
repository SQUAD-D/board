const keyWordInput = document.getElementById('key-word');
const searchBtn = document.getElementById('search-btn');
const searchPageContainer = document.getElementById('search-page-container');
const noContentContainer = document.getElementById('no-content');

searchBtn.addEventListener("click", () => {
    boardPageContainer.style.display = 'none';
    searchPageContainer.style.display = 'flex';
    const keyWord = keyWordInput.value;
    let searchType = document.getElementById('search-option');
    axios.get(`${homeUrl}/api/boards/search`, {
        params: {keyWord: keyWord, size: size, page: 1, searchType: searchType.options[searchType.selectedIndex].value}
    }).then(response => {
        const boards = response.data.contents;
        const firstPage = response.data.pagination.firstPage;
        const lastPage = response.data.pagination.lastPage;
        const totalContents = response.data.pagination.totalContent;
        boardTableContainer.innerHTML = '';
        noContentContainer.innerHTML = '';
        if (totalContents !== 0) {
            for (let i = 0; i < boards.length; i++) {
                boardTableContainer.innerHTML += `
            <tr>
                        <td onClick="location.href='${homeUrl}/boards/${boards[i].boardId}'">
                            ${boards[i].title}
                        </td>
                        <td>
                            <a>${boards[i].nickName}</a>
                        </td>
                        <td>
                            <a class="createdDate">${boards[i].createdDate}</a>
                        </td>
                    </tr>
        `
            }
            searchPageContainer.innerHTML = '';
            searchPageContainer.innerHTML += `
<span class="page"><a href="#" id="pre">Pre</a></span>`
            for (let i = firstPage; i < lastPage + 1; i++) {
                searchPageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
            }
            searchPageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>
`;
        } else {
            noContentContainer.innerHTML = '<h2>검색 결과가 없습니다.</h2>';
        }
    })
})

searchPageContainer.addEventListener("click", function (event) {
    event.preventDefault();
    const clickedElement = event.target;
    const keyWord = keyWordInput.value;
    let page = clickedElement.id;
    if (clickedElement.id === "pre") {
        page = currentPage - 1;
    }
    if (clickedElement.id === "next") {
        page = currentPage + 1;
    }
    axios.get(`${homeUrl}/api/boards/search`, {
        params: {keyWord: keyWord, size: size, page: page}
    }).then(response => {
        const boards = response.data.contents;
        currentPage = response.data.pagination.currentPage;
        const firstPage = response.data.pagination.firstPage;
        const lastPage = response.data.pagination.lastPage;

        boardTableContainer.innerHTML = '';
        for (let i = 0; i < boards.length; i++) {
            boardTableContainer.innerHTML += `
            <tr>
                        <td onClick="location.href='${homeUrl}/boards/${boards[i].boardId}'">
                            ${boards[i].title}
                        </td>
                        <td>
                            <a>${boards[i].nickName}</a>
                        </td>
                        <td>
                            <a class="createdDate">${boards[i].createdDate}</a>
                        </td>
                    </tr>
        `
        }
        searchPageContainer.innerHTML = '';
        searchPageContainer.innerHTML = `
<span class="page"><a href="#" id="pre">Pre</a></span>`
        for (let i = firstPage; i < lastPage + 1; i++) {
            searchPageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
        }
        searchPageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>
`;
    }).catch(error => {
        const data = error.response.data;
        if (data.code === 303) {
            alert(data.message);
        }
    })
});