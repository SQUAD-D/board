const deleteReqBtn = document.getElementById("delete-req-btn");
const updateReqBtn = document.getElementById("update-req-btn");
const boardId = document.getElementById("boardId");

deleteReqBtn.addEventListener("click", () => {
    const id = boardId.textContent;
    if (confirm("정말 삭제하시겠습니까?") === true) {
        axios.delete("http://localhost:8080/boards/" + id)
            .then(response => {
                const data = response.data;
                if (data.code === 301) {
                    window.location.href = '/boards';
                }
            }).catch(error => {
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
    axios.get("http://localhost:8080/boards/update/" + id)
        .then(response => {
            const htmlContent = response.data;
            document.open();
            document.write(htmlContent);
            document.close();
        })
        .catch(error => {
            const data = error.response.data;
            if (data.code === 302) {
                alert(data.message)
                return true;
            }
        })
})