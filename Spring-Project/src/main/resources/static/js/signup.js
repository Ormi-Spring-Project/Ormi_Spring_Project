function comparePassword() {
    let password = document.getElementById("password").value;
    let passwordConfirm = document.getElementById("password-confirm").value;

    if (password !== passwordConfirm) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    return true;
}