const signUpIdInput = document.getElementById("signUpId");
const signUpPwInput = document.getElementById("signUpPw");
const signUpNameInput = document.getElementById("signUpName");
const signUpNickNameInput = document.getElementById("signUpNickName");
const signUpBtn = document.getElementById("sign-up-btn");
const validationBtn = document.getElementById("validation-btn");
const nickValidationBtn = document.getElementById('nick-validation-btn');
// 중복아이디 검증
validationBtn.addEventListener("click", () => {
    const loginId = signUpIdInput.value;
    axios.post(`${homeUrl}/api/members/id-validation`, null, {
        params: {loginId: loginId}
    })
        // 중복아이디 X
        .then((response) => {
            const statusCode = response.status;
            if (statusCode === 200) {
                alert("사용가능한 아이디입니다.")
            }
        })
        // 중복아이디 O
        .catch(error => {
            const data = error.response.data;
            if (data.code === 103) {
                alert("중복된 아이디입니다.")
            }
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }
        })
})

// 중복닉네임 검증
nickValidationBtn.addEventListener("click", () => {
    const nickName = signUpNickNameInput.value;
    axios.post(`${homeUrl}/api/members/nick-validation`, null, {
        params: {nickName: nickName}
    })
        // 중복아이디 X
        .then((response) => {
            const statusCode = response.status;
            if (statusCode === 200) {
                alert("사용가능한 닉네임입니다.")
            }
        })
        // 중복아이디 O
        .catch(error => {
            const data = error.response.data;
            if (data.code === 103) {
                alert("중복된 닉네임 입니다.")

            }
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }
        })
})


signUpBtn.addEventListener("click", () => {
    const signUpId = signUpIdInput.value;
    const signUpPw = signUpPwInput.value;
    const signUpName = signUpNameInput.value;
    const signUpNickName = signUpNickNameInput.value;

    axios.post(`${homeUrl}/api/members`, {
        loginId: signUpId,
        loginPw: signUpPw,
        name: signUpName,
        nickName: signUpNickName
    })
        // 회원 가입 성공
        .then((response) => {
            const statusCode = response.status;
            if (statusCode === 200) {
                alert("회원가입이 완료, 로그인 후 사용해주세요.")
                window.location.href = '/';
            }
        })
        // 중복된 아이디 예외
        .catch(error => {
            const data = error.response.data;
            if (data.code === 103) {
                alert(data.message)
            }
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }

            if (data.code === 105) {
                alert(data.message)
            }
        })
});
