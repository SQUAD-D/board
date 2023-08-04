const loginIdInput = document.getElementById("loginId");
const loginPwInput = document.getElementById("loginPw");
const nameInput = document.getElementById("name");
const nickNameInput = document.getElementById("nickName");
const submitBtn = document.getElementById("submit-btn");
const validationBtn = document.getElementById("validation-btn");
let isValidateId = false;

// 중복아이디 검증
validationBtn.addEventListener("click", () => {
    const loginId = loginIdInput.value;
    axios.post("http://localhost:8080/members/validation", {
        loginId: loginId
    })
        .then((response) => {
            const result = response.data;
            console.log(result);
            if (result === "success") {
                alert("사용가능한 아이디 입니다.")
                isValidateId = true;
            }else{
                alert("중복된 아이디입니다.")
            }
        })
})


submitBtn.addEventListener("click", () => {
    if (isValidateId === false) {
        alert("중복아이디 체크가 필요합니다")
        return
    }
    const loginId = loginIdInput.value;
    const loginPw = loginPwInput.value;
    const name = nameInput.value;
    const nickName = nickNameInput.value;

    axios.post("http://localhost:8080/members/new", {
        loginId: loginId,
        loginPw: loginPw,
        name: name,
        nickName: nickName
    })
        .then((response) => {
            console.log(response)
        })
});

