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
        .then(() => {
            isValidateInfo = true;
            window.location.href = '/boards';
        })
        .catch(error => {
            const data = error.response.data;
            if (data.code === 101) {
                alert(data.message);
            }
        })
});

