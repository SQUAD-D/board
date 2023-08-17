const id = document.getElementById("boardId");
const updateBtn = document.getElementById("update-btn");
const titleInput = document.getElementById("title")
const contentInput = document.getElementById("content")

updateBtn.addEventListener("click", () => {
    const boardId = id.textContent;
    const title = titleInput.value;
    const content = contentInput.value;
    axios.patch("http://localhost:8080/boards/" + boardId, {
        title: title,
        content: content,
    })
        .then(response => {
            window.location.href = "http://localhost:8080/boards/" + boardId
        })
})
