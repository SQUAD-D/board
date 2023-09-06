const writeBtn = document.getElementById('comment-write');
const contentInput = document.getElementById('comment-content');
const id = document.getElementById("boardId");
const commentsContainer = document.getElementById('comment-container')

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
    const boardId = id.textContent;
    if (clickedElement.id === 'delete-comment') {
        if (confirm("정말 삭제하시겠습니까?")) {
            const commentId = getCommentId(clickedElement);
            const commentElement = clickedElement.closest('.comment-content');
            axios.delete(`http://localhost:8080/api/boards/${boardId}/comments/${commentId}`)
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
    if (clickedElement.id === 'update-comment') {
        const commentElement = clickedElement.closest('.comment-content');
        const content = commentElement.querySelector('.content').innerText;
        commentElement.innerHTML =
            `
            <div class="update-form">
                <p hidden="hidden" id="comment-id">${commentId}</p>
                <input type="text" value="${content}" placeholder="댓글을 입력해주세요." id="update-content"/>
                <button class="comment-writeBtn" id="comment-write">작성</button>
            </div>`
    }
})

// 댓글 수정
commentsContainer.addEventListener("click", function (event) {
    const clickedElement = event.target;
    if (clickedElement.id === 'comment-write') {
        const boardId = id.textContent;
        const inputElement = document.getElementById('update-content');
        const hiddenCommentId = document.getElementById('comment-id');
        const commentId = hiddenCommentId.textContent;
        const content = inputElement.value;
        axios.patch(`http://localhost:8080/api/boards/${boardId}/comments/${commentId}`, {
            content
        })
            .then(() => {
                location.reload();
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


// 댓글의 PK값 추출
function getCommentId(element) {
    const commentIdElement = element.closest('.comment-content').querySelector('[hidden="hidden"]');
    if (commentIdElement) {
        return commentIdElement.textContent;
    }
    return null;
}

// 게시글 댓글 리스트 랜더링
document.addEventListener("DOMContentLoaded", function () {
    displayComments()
})

// 화면에 렌더링
function displayComments() {
    const boardId = id.textContent;
    axios.get(`http://localhost:8080/api/boards/${boardId}/comments`)
        .then(response => {
            const comments = response.data.comments;
            const size = comments.length;
            for (let i = 0; i < size; i++) {
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
                </div>`
            }
        })
}
