const deleteReqBtn = document.getElementById("delete-req-btn");
const updateReqBtn = document.getElementById("update-req-btn");
const boardId = document.getElementById("boardId");

deleteReqBtn.addEventListener("click", () => {
    const id = boardId.textContent;
    if (confirm("정말 삭제하시겠습니까?") === true) {
        axios.delete(`http://localhost:8080/api/boards/${id}`)
            // 삭제 성공 후 리다이렉션
            .then(response => {
                const statusCode = response.status;
                if (statusCode === 200) {
                    window.location.href = '/boards';
                }
            })
            // 작성자 이외 수정/삭제 불가
            .catch(error => {
                const data = error.response.data;
                if (data.code === 302) {
                    alert(data.message)
                    return true;
                }
            })
    } else {
        return false;
    }
})

updateReqBtn.addEventListener("click", () => {
    const id = boardId.textContent;
    window.location.href = `http://localhost:8080/boards/update/${id}`
})

