const titleInput = document.getElementById("title");
// const contentInput = document.getElementById("content");
const contentInput = document.getElementById("editor");
const writeBtn = document.getElementById("write-btn");
const imageSelector = document.getElementById('img-selector');
let imageInfoList = [];
// const formData = new FormData();

// writeBtn.addEventListener("click", () => {
//
//   while (contentInput.firstChild) {
//     contentInput.removeChild(contentInput.firstChild);
//   }
//
//   const title = titleInput.value;
//   const content = contentInput.innerHTML;
//   const data = {
//     "title": title,
//     "content": content,
//     "imageInfo": imageInfoList
//   }
//   formData.append('createBoard',
//       new Blob([JSON.stringify(data)], {type: 'application/json'}));
//
//   axios.post(`${homeUrl}/api/boards`, formData, {
//     headers: {
//       'Content-Type': 'multipart/form-data'
//     }
//   }).then(response => {
//     const statusCode = response.status;
//     const data = response.data;
//     // 게시글 작성 성공
//     if (statusCode === 200) {
//       window.location.href = `/boards/${data.id}`;
//     }
//   }).catch(error => {
//     const data = error.response.data;
//     // 필드 에러
//     if (data.code === 500) {
//       alert(data.fieldErrorMessage)
//     }
//   })
// })

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
    const data = response.data;
    // 게시글 작성 성공
    if (statusCode === 200) {
      window.location.href = `/boards/${data.id}`;
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

// imageSelector.addEventListener('change', function (e) {
//   const files = e.target.files;
//   formData.append("images", files[0]);
//   const reader = new FileReader();
//   reader.onload = function (event) {
//     // 이미지 태그를 생성하고 src 속성을 설정
//     const img = document.createElement('img');
//     img.src = event.target.result;
//     img.style.maxWidth = '200px'; // 이미지 크기를 조절할 수 있습니다
//     img.style.maxHeight = '200px';
//
//     // contentInput 요소에 이미지 태그를 추가
//     contentInput.appendChild(img);
//   }
//   reader.readAsDataURL(files[0]);
// });

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
    img.style.width = '200px';
    contentInput.appendChild(img);
  }).catch(error => {
    const data = error.response.data;
    alert(data.message);
  })
});

