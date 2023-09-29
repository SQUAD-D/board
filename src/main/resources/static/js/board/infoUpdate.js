const signUpIdInput = document.getElementById("signUpId");
const signUpPwInput = document.getElementById("signUpPw");
const signUpNameInput = document.getElementById("signUpName");
const signUpNickNameInput = document.getElementById("signUpNickName");
const validationBtn = document.getElementById("validation-btn");
const nickValidationBtn = document.getElementById('nick-validation-btn');
const updateBtn = document.getElementById('update-btn');

document.addEventListener("DOMContentLoaded", () => {
    axios.get(`${homeUrl}/api/members`)
        .then(response => {
            const data = response.data;
            signUpIdInput.value = data.loginId;
            signUpPwInput.value = data.loginPw;
            signUpNameInput.value = data.name;
            signUpNickNameInput.value = data.nickName;
        })
})

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
                alert(data.message)
            }
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }
        })
})

// 중복아이디 검증
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
            if (data.code === 104) {
                alert(data.message)
            }
            // 필드 에러
            if (data.code === 500) {
                alert(data.fieldErrorMessage)
            }
        })
})

updateBtn.addEventListener("click", () => {
    const loginId = signUpIdInput.value;
    const loginPw = signUpPwInput.value;
    const name = signUpNameInput.value;
    const nickName = signUpNickNameInput.value;

    axios.patch(`${homeUrl}/api/members`, {
            loginId,
            loginPw,
            name,
            nickName,
        }
    ).then(response => {
        const status = response.status;
        if (status === 200) {
            alert("수정완료");
            window.location.reload()
        }
    })
        .catch(error => {
            const data = error.response.data;
            if (data.code === 500) {
                alert(data.fieldErrorMessage);
            }
            if (data.code === 105) {
                alert(data.message)
            }
        })
})