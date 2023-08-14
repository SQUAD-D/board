const signUpIdInput = document.getElementById("signUpId");
const signUpPwInput = document.getElementById("signUpPw");
const signUpNameInput = document.getElementById("signUpName");
const signUpNickNameInput = document.getElementById("signUpNickName");
const signUpBtn = document.getElementById("sign-up-btn");
const validationBtn = document.getElementById("validation-btn");
let isValidateId = false;

// 중복아이디 검증
validationBtn.addEventListener("click", () => {
    const signUpId = signUpIdInput.value;
    axios.post("http://localhost:8080/members/validation", {
        loginId: signUpId
    })
        .then((response) => {
            const data = response.data
            const message = data.message
            const code = data.code
            if (code === 102) {
                alert(message)
                isValidateId = true
            } else {
                alert(message)
            }
        })
})


signUpBtn.addEventListener("click", () => {
    if (isValidateId === false) {
        alert("중복아이디 체크가 필요합니다")
        return
    }
    const signUpId = signUpIdInput.value;
    const signUpPw = signUpPwInput.value;
    const signUpName = signUpNameInput.value;
    const signUpNickName = signUpNickNameInput.value;

    axios.post("http://localhost:8080/members/new", {
        loginId: signUpId,
        loginPw: signUpPw,
        name: signUpName,
        nickName: signUpNickName
    })
        .then((response) => {
            if (response.status === 200) {
                alert("회원가입이 완료, 로그인 후 사용해주세요.")
                window.location.href = '/';
            }
        })
});
