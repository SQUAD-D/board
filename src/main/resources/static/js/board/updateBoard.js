const id = document.getElementById("boardId");
const updateBtn = document.getElementById("update-btn");
const titleInput = document.getElementById("title")
const contentInput = document.getElementById("content")

// url 경로 파싱
const pathString = window.location.pathname;
const segments = pathString.split("/");
const boardId = segments[3];

updateBtn.addEventListener("click", () => {
    const title = titleInput.value;
    const content = contentInput.value;
    axios.patch(`${homeUrl}/api/boards/${boardId}`, {
        title,
        content,
    })
        .then(() => {
            window.location.href = `${homeUrl}/boards/${boardId}`
        })
        .catch(error => {
            const data = error.response.data;
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }
            if (data.code === 302) {
                alert(data.message);
            }
        })
})

document.addEventListener("DOMContentLoaded", () => {
    axios.get(`${homeUrl}/api/boards/${boardId}`)
        .then(response => {
            const data = response.data;
            titleInput.value = data.title;
            contentInput.textContent = data.content;
        })
})