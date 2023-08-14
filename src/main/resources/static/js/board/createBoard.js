const titleInput = document.getElementById("title");
const contentInput = document.getElementById("content");
const writeBtn = document.getElementById("write-btn");

writeBtn.addEventListener("click", () => {
    const title = titleInput.value;
    const content = contentInput.value;
    axios.post("http://localhost:8080/boards/new", {
        title: title,
        content: content
    }).then(response => {
        const data = response.data;
        if (data.code === 300) {
            window.location.href = '/boards';
        }
    })
})
