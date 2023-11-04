const titleInput = document.getElementById("title");
const contentInput = document.getElementById("content");
const writeBtn = document.getElementById("write-btn");
const FileElement = document.getElementById("file");

writeBtn.addEventListener("click", () => {
    const title = titleInput.value;
    const content = contentInput.value;
    const formData = new FormData()
    const data = {
        "title": title,
        "content": content
    }
    formData.append("data", new Blob([JSON.stringify(data)], {
        type: "application/json"
    }));
    formData.append("image", FileElement.files[0]);
    axios.post(`${homeUrl}/api/boards`,
        formData
        , {
            headers: {
                "Content-Type": "multipart/form-data"
            }
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

// 이미지 첨부기능

const inputImage = document.getElementById("file")
inputImage.addEventListener("change", e => {
    readImage(e.target)
})

function readImage(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader()
        reader.onload = e => {
            const previewImage = document.getElementById("preview-image")
            previewImage.src = e.target.result;
        }
        reader.readAsDataURL(input.files[0])
    }
}
