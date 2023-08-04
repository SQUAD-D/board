const loginIdInput = document.getElementById("loginId");
const loginPwInput = document.getElementById("loginPw");
const submitBtn = document.getElementById("submit-btn");
let isValidateInfo = false;

submitBtn.addEventListener("click", () => {
    const loginId = loginIdInput.value;
    const loginPw = loginPwInput.value;

    axios.post("http://localhost:8080/members/login", {
        loginId: loginId,
        loginPw: loginPw,
    })
        .then((response) => {
            const result = response.data;
            if(result === "success"){
                console.log(result);
                isValidateInfo = true;
                window.location.href='/';
            }else{
                alert("로그인 정보를 다시 확인해주세요.");
            }
        })
});

