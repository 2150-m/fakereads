let iFName = document.getElementById("first_name");
let iLName = document.getElementById("last_name");
let iUName = document.getElementById("username");
let iDOB = document.getElementById("dob");
let iDesc = document.getElementById("description");
let iMail = document.getElementById("mail");
let iPass = document.getElementById("password");
let iPhoto = document.getElementById("photo");
let btnAdd = document.getElementById("btn_add");
let p_status = document.getElementById("p_status");

async function apiAdd(vFName, vLName, vUName, vDOB, vDesc, vMail, vPass, vPhoto) {
    const response = await fetch('/api/admin/addauthor', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: vFName,
            lastName: vLName,
            username: vUName,
            dateOfBirth: vDOB,
            description: vDesc,
            mailAddress: vMail,
            password: vPass,
            profilePic: vPhoto
        })
    });

    const text = await response.text();
    console.log(response);
    console.log(text);

    if (response.status == '200') {
        p_status.innerHTML = "Author \"" + vFName + " " + vLName + "\" has been added.";
        return;
    }
    p_status.innerHTML = text;
}

function add() {
    // let photo = iPhoto.value;
    // if (photo == "") {
    //     photo = "/covers/404.png";
    // }
    let photo = "/covers/default.png";

    apiAdd(iFName.value, iLName.value, iUName.value, iDOB.value, iDesc.value, iMail.value, iPass.value, photo);
}

btnAdd.onclick = add;