const writeBtn = document.getElementById('comment-write');
const contentInput = document.getElementById('comment-content');
const id = document.getElementById("boardId");
const commentsContainer = document.getElementById('comment-container')
const pageContainer = document.getElementById('page-container')
// 댓글 생성 후 댓글 리스트 랜더링
writeBtn.addEventListener("click", () => {
    const boardId = id.textContent;
    const content = contentInput.value;
    axios.post(`http://localhost:8080/api/boards/${boardId}/comments`, {
        content
    })
        .then(response => {
            const statusCode = response.status;
            if (statusCode === 201) {
                location.reload();
            }
        })
        .catch(error => {
            const data = error.response.data;
            alert(data.fieldErrorMessage)
        })
})

// 댓글 삭제
commentsContainer.addEventListener("click", function (event) {
    const clickedElement = event.target;
    event.preventDefault();
    if (clickedElement.id === 'delete-comment') {
        if (confirm("정말 삭제하시겠습니까?")) {
            const commentId = getCommentId(clickedElement);
            const commentElement = clickedElement.closest('.comment-content');
            axios.delete(`http://localhost:8080/api/boards/comments/${commentId}`)
                .then(() => {
                    commentElement.remove();
                })
                .catch(error => {
                    const data = error.response.data;
                    if (data.code === 400) {
                        alert(data.message);
                    }
                })
        }
    }
})

// 대댓글 삭제
commentsContainer.addEventListener("click", async function (event) {
    const clickedElement = event.target;
    event.preventDefault();
    if (clickedElement.id === 'delete-child-comment') {
        if (confirm("정말 삭제하시겠습니까?")) {
            const commentId = getChildCommentId(clickedElement);
            const commentElement = clickedElement.closest('.child-comment-content');
            axios.delete(`http://localhost:8080/api/boards/comments/${commentId}`)
                .then(() => {
                    commentElement.remove();
                })
                .catch(error => {
                    const data = error.response.data;
                    if (data.code === 400) {
                        alert(data.message);
                    }
                })
        }
    }
})

// 댓글 수정폼 요청
commentsContainer.addEventListener("click", function (event) {
    const clickedElement = event.target;
    event.preventDefault();
    const commentId = getCommentId(clickedElement)
    const commentElement = clickedElement.closest('.comment-content');
    const updateForm = commentElement.querySelector('.update-form')
    const content = commentElement.querySelector('.content').innerText;
    if (clickedElement.id === 'update-comment') {
        if (updateForm.style.display === "none") {
            updateForm.style.display = "block";
            updateForm.innerHTML =
                `
            <div class="update-form">
                <p hidden="hidden" id="comment-id">${commentId}</p>
                <input type="text" value="${content}" placeholder="댓글을 입력해주세요." id="update-content"/>
                <button class="comment-writeBtn" id="comment-write">작성</button>
            </div>`
        } else {
            updateForm.style.display = "none";
            updateForm.innerHTML = '';
        }
    }
})

// 대댓글 수정폼 요청
commentsContainer.addEventListener("click", function (event) {
    const clickedElement = event.target;
    event.preventDefault();
    if (clickedElement.id === 'update-child-comment') {
        const commentId = getChildCommentId(clickedElement)
        const commentElement = clickedElement.closest('.child-comment-content');
        const updateForm = commentElement.querySelector('.child-update-form')
        const content = commentElement.querySelector('.content').innerText;
        if (updateForm.style.display === "none") {
            updateForm.style.display = "block";
            updateForm.innerHTML =
                `
            <div class="update-form">
                <p hidden="hidden" id="comment-id">${commentId}</p>
                <input type="text" value="${content}" placeholder="댓글을 입력해주세요." id="update-content"/>
                <button class="comment-writeBtn" id="child-comment-write">작성</button>
            </div>`
        } else {
            updateForm.style.display = "none";
            updateForm.innerHTML = '';
        }
    }
})

// 댓글 수정
commentsContainer.addEventListener("click", function (event) {
    const clickedElement = event.target;
    if (clickedElement.id === 'comment-write') {
        const comment = clickedElement.closest('.comment-content');
        const updatedContent = comment.querySelector('.content');
        const inputElement = document.getElementById('update-content');
        const hiddenCommentId = document.getElementById('comment-id');
        const commentId = hiddenCommentId.textContent;
        const content = inputElement.value;
        axios.patch(`http://localhost:8080/api/boards/comments/${commentId}`, {
            content
        })
            .then(() => {
                updatedContent.innerHTML = `<p>${content}</p>`
            })
            .catch(error => {
                const data = error.response.data;
                if (data.code === 500) {
                    alert(data.fieldErrorMessage);
                }
                if (data.code === 400) {
                    alert(data.message);
                    location.reload();
                }
            })
    }
})

// 대댓글 수정
commentsContainer.addEventListener("click", function (event) {
    const clickedElement = event.target;
    if (clickedElement.id === 'child-comment-write') {
        const comment = clickedElement.closest('.child-comment-content');
        const updatedContent = comment.querySelector('.content');
        const inputElement = document.getElementById('update-content');
        const hiddenCommentId = document.getElementById('comment-id');
        const commentId = hiddenCommentId.textContent;
        const content = inputElement.value;
        axios.patch(`http://localhost:8080/api/boards/comments/${commentId}`, {
            content
        })
            .then(() => {
                updatedContent.innerHTML = `<p>${content}</p>`
            })
            .catch(error => {
                const data = error.response.data;
                if (data.code === 500) {
                    alert(data.fieldErrorMessage);
                }
                if (data.code === 400) {
                    alert(data.message);
                    location.reload();
                }
            })
    }
})

// 대댓글 표출
commentsContainer.addEventListener("click", function (event) {
        getChildComments(event);
    }
)

// 대댓글 생성
commentsContainer.addEventListener("click", async function (event) {
    const clickedElement = event.target;
    const parentCommentId = getCommentId(clickedElement);
    const boardId = id.textContent;
    const contentInput = document.getElementById(`reply-content${parentCommentId}`);
    const content = contentInput.value;
    if (clickedElement.id === `reply-comment-writeBtn${parentCommentId}`) {
        await axios.post(`http://localhost:8080/api/boards/${boardId}/comments`, {
            content,
            parentCommentId
        })
        const commentElement = clickedElement.closest('.comment-content');
        const childCommentList = commentElement.querySelector(`.child-comments${parentCommentId}`)
        childCommentList.innerHTML = '';
        await axios.get(`http://localhost:8080/api/boards/${boardId}/childComments/${parentCommentId}`)
            .then(response => {
                const comments = response.data.contents;
                for (let i = 0; i < contents.length; i++) {
                    childCommentList.innerHTML
                        += `<div class="child-comment-content">
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
                        <a href="#" id="update-child-comment">수정</a>
                        <a href="#" id="delete-child-comment">삭제</a>
                    </div>
                    <div style="display: none" class="child-update-form"></div>`
                }
            })
    }
})

// 댓글의 PK값 추출
function getCommentId(element) {
    const commentIdElement = element.closest('.comment-content').querySelector('[hidden="hidden"]');
    if (commentIdElement) {
        return commentIdElement.textContent;
    }
    return null;
}

// 대댓글의 PK값 추출
function getChildCommentId(element) {
    const commentIdElement = element.closest('.child-comment-content').querySelector('[hidden="hidden"]');
    if (commentIdElement) {
        return commentIdElement.textContent;
    }
    return null;
}

function getChildComments(event) {
    const clickedElement = event.target;
    const commentElement = clickedElement.closest('.comment-content');
    const childComment = commentElement.querySelector(".child-comment");
    const parentCommentId = getCommentId(clickedElement);
    const childCommentList = commentElement.querySelector(`.child-comments${parentCommentId}`)
    const boardId = id.textContent;
    if (clickedElement.id === 'reply') {
        if (childComment.style.display === "none") {
            childComment.style.display = "block";
            childCommentList.innerHTML = '';
            axios.get(`http://localhost:8080/api/boards/${boardId}/childComments/${parentCommentId}`)
                .then(response => {
                    const comments = response.data.contents;
                    for (let i = 0; i < comments.length; i++) {
                        childCommentList.innerHTML
                            += `<div class="child-comment-content">
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
                        <a href="#" id="update-child-comment">수정</a>
                        <a href="#" id="delete-child-comment">삭제</a>
                    </div>
                    <div style="display: none" class="child-update-form"></div>`
                    }
                })
        } else {
            childComment.style.display = "none";
        }
    }
}

// 게시글 댓글 리스트 랜더링
document.addEventListener("DOMContentLoaded", function () {
    displayComments()
})

let size = 5;

// 화면에 렌더링 [첫 접근]
function displayComments() {
    const boardId = id.textContent;
    let page;
    axios.get(`http://localhost:8080/api/boards/${boardId}/comments`, {
        params: {size: size, page: 1}
    })
        .then(response => {
            const comments = response.data.contents;
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
                        <a href="#" id="reply">답글</a>
                        <a href="#" id="update-comment">수정</a>
                        <a href="#" id="delete-comment">삭제</a>
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
        })
}

let currentPage = 1;
// 페이지 접근
pageContainer.addEventListener("click", function (event) {
    event.preventDefault();
    const boardId = id.textContent;
    const clickedElement = event.target;
    let page = clickedElement.id;
    if (clickedElement.id === "pre") {
        page = currentPage - 1;
    }
    if (clickedElement.id === "next") {
        page = currentPage + 1;
    }
    axios.get(`http://localhost:8080/api/boards/${boardId}/comments`, {
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
                        <a href="#" id="reply">답글</a>
                        <a href="#" id="update-comment">수정</a>
                        <a href="#" id="delete-comment">삭제</a>
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
        if (data.code === 401) {
            alert(data.message);
        }
    })
})
