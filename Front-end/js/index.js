import { authenticate, signup } from "./auth.js";

document.onreadystatechange = () => {
    $("#signin-b").click(authenticate);
    $("#signup-b").click(signup);
    $("#sign-up").hide();
    $("#lnk-sup").click(toggleSignUp);
    $("#lnk-sin").click(toggleSignUp);
}

const toggleSignUp = () => {
    $("#sign-up").toggle();
    $("#sign-in").toggle();
}