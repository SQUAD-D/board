const boardTableContainer = document.getElementById('board-table');
const boardPageContainer = document.getElementById('page-container');
const noContent = document.getElementById('no-content')

function getUrl() {
    const mainBoards = 'http://localhost:8080/api/boards';
    const myBoards = 'http://localhost:8080/api/my-page/my-boards'
    const mainComments = '';
    const myComments = '';
    const pathname = location.pathname;
    let url;
    if (pathname === '/boards') {
        return url = mainBoards;
    } else {
        return url = myBoards;
    }
}

let size = 15;
// 첫 페이지 렌더링
document.addEventListener("DOMContentLoaded", function () {
    searchPageContainer.style.display = 'none';
    const url = getUrl();
    axios.get(url, {
        params: {size: size, page: 1}
    }).then(response => {
        const boards = response.data.contents;
        const totalContent = response.data.pagination.totalContent;
        const firstPage = response.data.pagination.firstPage;
        const lastPage = response.data.pagination.lastPage;
        boardTableContainer.innerHTML = '';
        if (totalContent !== 0) {
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
            boardPageContainer.innerHTML = '';
            boardPageContainer.innerHTML = `
<span class="page"><a href="#" id="pre">Pre</a></span>`
            for (let i = firstPage; i < lastPage + 1; i++) {
                boardPageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
            }
            boardPageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>`;
        } else {
            noContentContainer.innerHTML = '<h2>작성한 게시글이 없습니다.</h2>';
        }

    })
})

let currentPage = 1;
boardPageContainer.addEventListener("click", function (event) {
    event.preventDefault();
    const clickedElement = event.target;
    let page = clickedElement.id;
    if (clickedElement.id === "pre") {
        page = currentPage - 1;
    }
    if (clickedElement.id === "next") {
        page = currentPage + 1;
    }
    const url = getUrl();
    axios.get(url, {
        params: {size: size, page: page}
    }).then(response => {
        const boards = response.data.contents;
        currentPage = response.data.pagination.currentPage;
        const firstPage = response.data.pagination.firstPage;
        const lastPage = response.data.pagination.lastPage;
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
        boardPageContainer.innerHTML = `
<span class="page"><a href="#" id="pre">Pre</a></span>`
        for (let i = firstPage; i < lastPage + 1; i++) {
            boardPageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
        }
        boardPageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>
`;
    }).catch(error => {
        const data = error.response.data;
        if (data.code === 303) {
            alert(data.message);
        }
    })
})

