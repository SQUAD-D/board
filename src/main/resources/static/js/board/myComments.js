const commentsContainer = document.getElementById('comment-container')
const pageContainer = document.getElementById('page-container')
const noContent = document.getElementById('no-content');
const size = 10;

document.addEventListener("DOMContentLoaded", function () {
    axios.get(`http://localhost:8080/api/my-page/my-comments`, {
        params: {size: size, page: 1}
    })
        .then(response => {
            const comments = response.data.contents;
            const totalComments = response.data.pagination.totalContent;
            const firstPage = response.data.pagination.firstPage;
            const lastPage = response.data.pagination.lastPage;
            commentsContainer.innerHTML = '';
            if (totalComments !== 0) {
                for (let i = 0; i < comments.length; i++) {
                    commentsContainer.innerHTML
                        += `<div class="comment-content">
                    <p hidden="hidden">${comments[i].commentId}</p>
                    <p hidden="hidden">${comments[i].parentCommentId}</p>
                    <div class="member-nickName">
                        <h3>${comments[i].nickName}</h3>
                    </div>
                    <div class="createdDate">
                        <p>작성일 : ${comments[i].createdDate}</p>
                    </div>
                    ${comments[i].modifiedDate !== null ? `<div class="modifiedDate">
                        <p>수정됨</p>
                    </div>` : ''}                   
                    <div class="content" id="content">
                        <p>${comments[i].content}</p>
                    </div>
                    <div class="comment-menu">
                        <a onclick="location.href='/boards/${comments[i].boardId}'">게시글 이동</a>
                    </div>
                    <div style="display: none" class="update-form">
                   
                    </div>
                    <div style="display: none" class="child-comment">
                    <div class="child-comments${comments[i].commentId}">
                   
                    </div>
                    <input type="text" placeholder="답글을 입력해주세요." id="reply-content${comments[i].commentId}"/>
                    <button id="reply-comment-writeBtn${comments[i].commentId}">작성</button>
                    </div>
                </div>`
                }
                pageContainer.innerHTML = `<span class="page"><a href="#" id="pre">Pre</a></span>`;
                for (let i = firstPage; i < lastPage + 1; i++) {
                    pageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
                }
                pageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>`;
            } else {
                noContent.innerHTML = '<h3>댓글이 없습니다.</h3>'
            }

        })
})

let currentPage = 1;
// 페이지 접근
pageContainer.addEventListener("click", function (event) {
    event.preventDefault();
    const clickedElement = event.target;
    let page = clickedElement.id;
    if (clickedElement.id === "pre") {
        page = currentPage - 1;
    }
    if (clickedElement.id === "next") {
        page = currentPage + 1;
    }
    axios.get(`http://localhost:8080/api/my-page/my-comments`, {
        params: {size: size, page: page}
    }).then(response => {
        const comments = response.data.contents;
        currentPage = response.data.pagination.currentPage;
        const firstPage = response.data.pagination.firstPage;
        const lastPage = response.data.pagination.lastPage;
        commentsContainer.innerHTML = '';
        for (let i = 0; i < comments.length; i++) {
            commentsContainer.innerHTML
                += `<div class="comment-content">
                    <p hidden="hidden">${comments[i].commentId}</p>
                    <p hidden="hidden">${comments[i].parentCommentId}</p>
                    <div class="member-nickName">
                        <h3>${comments[i].nickName}</h3>
                    </div>
                    <div class="createdDate">
                        <p>작성일 : ${comments[i].createdDate}</p>
                    </div>
                    ${comments[i].modifiedDate !== null ? `<div class="modifiedDate">
                        <p>수정됨</p>
                    </div>` : ''}                   
                    <div class="content" id="content">
                        <p>${comments[i].content}</p>
                    </div>
                    <div class="comment-menu">
                        <a onclick="location.href='/boards/${comments[i].boardId}'">게시글 이동</a>
                       
                    </div>
                    <div style="display: none" class="update-form">
                   
                    </div>
                    <div style="display: none" class="child-comment">
                    <div class="child-comments${comments[i].commentId}">
                   
                    </div>
                    <input type="text" placeholder="답글을 입력해주세요." id="reply-content${comments[i].commentId}"/>
                    <button id="reply-comment-writeBtn${comments[i].commentId}">작성</button>
                    </div>
                </div>`
        }
        pageContainer.innerHTML = '';

        pageContainer.innerHTML += `<span class="page"><a href="#" id="pre">Pre</a></span>`;
        for (let i = firstPage; i < lastPage + 1; i++) {
            pageContainer.innerHTML += `<span class="page"><a href="#" id="${i}">${i}</a></span>`
        }
        pageContainer.innerHTML += `<span><a href="#" id="next">Next</a></span>`;
        const pageNumber = pageContainer.closest(`#${page}`);
        pageNumber.style.backgroundColor = '#5685ff';

    }).catch(error => {
        const data = error.response.data;
        if (data.code === 303) {
            alert(data.message);
        }
    })
})
