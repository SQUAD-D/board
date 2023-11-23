const id = document.getElementById("boardId");
const updateBtn = document.getElementById("update-btn");
const titleInput = document.getElementById("title")
const contentInput = document.getElementById("editor")
const imageSelector = document.getElementById('img-selector');
let imageInfoList = [];

// url 경로 파싱
const pathString = window.location.pathname;
const segments = pathString.split("/");
const boardId = segments[3];

updateBtn.addEventListener("click", () => {
    const title = titleInput.value;
    const content = contentInput.innerHTML;
    axios.patch(`${homeUrl}/api/boards/${boardId}`, {
        title,
        content,
        imageInfoList
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
            contentInput.innerHTML = data.content;
        })
})

// 이미지 첨부기능
const btnImage = document.getElementById('btn-image');
btnImage.addEventListener('click', function () {
    imageSelector.click();
});

imageSelector.addEventListener('change', function (e) {
    const files = e.target.files;
    const formData = new FormData();
    let imgSrc;
    formData.append("image", files[0]);
    axios.post(`${homeUrl}/api/boards/img`,
        formData
        , {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }
    ).then(response => {
        imgSrc = response.data.imageSrc;
        imageInfoList.push(
            {
                imageUUID: response.data.imageUUID,
                imageSize: response.data.imageSize,
                imageOriginalName: response.data.imageOriginalName
            });
        let img = document.createElement("img");
        img.src = imgSrc;
        img.style.width = '600px';
        contentInput.appendChild(img);
    }).catch(error => {
        const data = error.response.data;
        alert(data.message);
    })
});