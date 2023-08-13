const loginIdInput = document.getElementById("loginId");
const loginPwInput = document.getElementById("loginPw");
const loginBtn = document.getElementById("login-btn");
let isValidateInfo = false;

loginBtn.addEventListener("click", () => {
    const loginId = loginIdInput.value;
    const loginPw = loginPwInput.value;

    axios.post("http://localhost:8080/members/login", {
        loginId: loginId,
        loginPw: loginPw,
    })
        .then(response => {
            const data = response.data;
            const message = data.message;
            const code = data.code;
            if (code === 101) {
                alert(message);
                return
            }
            isValidateInfo = true;
            window.location.href = '/';
        })
});

