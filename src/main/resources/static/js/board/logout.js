const logoutBtn = document.getElementById("logOut");

logoutBtn.addEventListener("click", () => {
    axios.post("http://localhost:8080/members/logout")
        .then((response) => {
            const data = response.data;
            if (data.code === 104) {
                window.location.href = "/";
            }
        })
})