const keyWordInput = document.getElementById('key-word');
const searchBtn = document.getElementById('search-btn');
const searchPageContainer = document.getElementById('search-page-container');

searchBtn.addEventListener("click", () => {
    boardPageContainer.style.display = 'none';
    searchPageContainer.style.display = 'flex';
    const keyWord = keyWordInput.value;
    axios.get(`http://localhost:8080/api/boards/search`, {
        params: {keyWord: keyWord, size: size, page: 1}
    }).then(response => {
        const boards = response.data.boards;
        const firstPage = response.data.boardPaging.firstPage;
        const lastPage = response.data.boardPaging.lastPage;
        boardTableContainer.innerHTML = '';
        for (let i = 0; i < boards.length; i++) {
            boardTableContainer.innerHTML += `
            <tr>
                        <td onClick="location.href='http://localhost:8080/boards/${boards[i].boardId}'">
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
    axios.get('http://localhost:8080/api/boards/search', {
        params: {keyWord: keyWord, size: size, page: page}
    }).then(response => {
        const boards = response.data.boards;
        currentPage = response.data.boardPaging.currentPage;
        const firstPage = response.data.boardPaging.firstPage;
        const lastPage = response.data.boardPaging.lastPage;
        boardTableContainer.innerHTML = '';
        for (let i = 0; i < boards.length; i++) {
            boardTableContainer.innerHTML += `
            <tr>
                        <td onClick="location.href='http://localhost:8080/boards/${boards[i].boardId}'">
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