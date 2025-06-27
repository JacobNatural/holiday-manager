import React from "react";
import SubmitButton from "./SubmitButton";
import Button from "./Button";

const LoginForm = ({onSubmit, children, handleForgotButton}) => {
    return <form id="login" className="container" onSubmit={onSubmit}>
        <label htmlFor="username">Username:</label>
        <input type="text" id={"username"} name={"username"} required={true}/>
        <label htmlFor="password">Password:</label>
        <input type="password" id={"password"} name={"password"} required={true}/>
        <SubmitButton>Login</SubmitButton>
        <Button onClick={handleForgotButton}>Forgot password</Button></form>;
        };
    export default LoginForm;