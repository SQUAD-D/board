const boardTableContainer = document.getElementById('board-table');
const boardPageContainer = document.getElementById('page-container');

let size = 10;
// 첫 페이지 렌더링
document.addEventListener("DOMContentLoaded", function () {
    // boardPageContainer.style.display = 'block';
    searchPageContainer.style.display = 'none';
    axios.get('http://localhost:8080/api/boards', {
        params: {size: size, page: 1}
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
        boardPageContainer.innerHTML = `
<span class="page"><a href="#" id="pre">Pre</a></span>`
        for (let i = firstPage; i < lastPage + 1; i++) {
            boardPageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
        }
        boardPageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>`;
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
    axios.get('http://localhost:8080/api/boards', {
        params: {size: size, page: page}
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

