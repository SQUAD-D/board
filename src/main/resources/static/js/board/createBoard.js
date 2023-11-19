const titleInput = document.getElementById("title");
// const contentInput = document.getElementById("content");
const contentInput = document.getElementById("editor");
const writeBtn = document.getElementById("write-btn");
const imageSelector = document.getElementById('img-selector');
let imageInfoList = [];

writeBtn.addEventListener("click", () => {
    const title = titleInput.value;
    const content = contentInput.innerHTML;
    const data = {
        "title": title,
        "content": content,
        "imageInfo": imageInfoList
    }
    axios.post(`${homeUrl}/api/boards`, data
    ).then(response => {
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

