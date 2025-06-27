import {useNavigate} from "react-router-dom";
import React, {useContext} from "react";
import SendData from "../services/apiService";
import LoginForm from "../components/LoginForm";
import {AuthContext} from "../context/CreateContext";
import {ENDPOINTS} from "../api/ENDPOINTS";

const Login = () => {
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const form = e.target;

        const formData = new FormData(form);

        const data ={
            username: formData.get("username"),
            password: formData.get("password"),
        }

        try{
            await SendData(data, ENDPOINTS.LOGIN, 'POST',navigate)
            const resp = await SendData(null, ENDPOINTS.GET_ROLE, 'GET', navigate);

            if(!resp || !resp.data){
                throw Error("Role error")
            }

            if(resp.data === "ROLE_ADMIN"){
                localStorage.setItem('auth', JSON.stringify({auth, setAuth}))
                setAuth(true);
                navigate('/admin');
            }else if(resp.data === "ROLE_WORKER"){
                localStorage.setItem('auth', JSON.stringify({auth, setAuth}))
                setAuth(true);
                navigate('/worker');
            }else{
                throw Error("Role error");
            }
        }catch (err){
            console.log(err);
            alert('Login failed');
        }
    };

    const handleForgotButton = () =>{
        navigate('/forgot-password');
    }
        return (
            <LoginForm onSubmit={handleSubmit} handleForgotButton={handleForgotButton}/>
        );

}

export default Login;


