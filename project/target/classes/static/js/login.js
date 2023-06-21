let txt_username = document.getElementById("txt_username");
let txt_password = document.getElementById("txt_password");
let btn_login = document.getElementById("btn_login");
let loginform_status = document.getElementById("loginform_status");


txt_username.focus();
txt_username.select();


async function api_login(v_username, v_password) {
    const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({username: v_username, password: v_password})
    });


    const text = await response.text();

    console.log(response);

    console.log(text);
    loginform_status.innerHTML = text;

    if (response.ok) {
        window.location.href = "myaccount";
    }
}




function login() {
    api_login(txt_username.value, txt_password.value);
}


btn_login.onclick = login;

txt_username.addEventListener("keydown", function(event) { if (event.keyCode == 13) { txt_password.focus(); } }, false);
txt_password.addEventListener("keydown", function(event) { if (event.keyCode == 13) { login(); } }, false);




