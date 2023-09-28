const logoutBtn = document.getElementById("logOut");

logoutBtn.addEventListener("click", () => {
    axios.post("http://localhost:8080/api/members/logout")
        // 로그아웃 성공
        .then((response) => {
            const statusCode = response.status;
            if (statusCode === 200) {
                window.location.href = "/";
            }
        })
})