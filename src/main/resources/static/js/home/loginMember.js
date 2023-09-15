const loginIdInput = document.getElementById("loginId");
const loginPwInput = document.getElementById("loginPw");
const loginBtn = document.getElementById("login-btn");

loginBtn.addEventListener("click", () => {
    const loginId = loginIdInput.value;
    const loginPw = loginPwInput.value;

    axios.post("http://localhost:8080/api/members/login", {
        loginId,
        loginPw,
    })
        .then(() => {
            // 첫 화면은 1페이지 15건의 게시글
            window.location.href = '/boards';
        })
        // 잘못된 로그인 요청 (아이디, 비밀번호 불일치)
        .catch(error => {
            const data = error.response.data;
            if (data.code === 101) {
                alert(data.message);
            }
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }
        })
});

