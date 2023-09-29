const titleInput = document.getElementById("title");
const contentInput = document.getElementById("content");
const writeBtn = document.getElementById("write-btn");

writeBtn.addEventListener("click", () => {
    const title = titleInput.value;
    const content = contentInput.value;
    axios.post(`${homeUrl}/api/boards`, {
        title,
        content
    }).then(response => {
        const statusCode = response.status;
        // 게시글 작성 성공
        if (statusCode === 200) {
            window.location.href = '/boards';
        }
    }).catch(error => {
        const data = error.response.data;
        // 필드 에러
        if (data.code === 500) {
            alert(data.fieldErrorMessage)
        }
    })
})
