import React from "react";
import {useNavigate} from 'react-router-dom';
import SubmitButton from '../components/SubmitButton'

const Home = () => {
    const navigate = useNavigate();

    const handleRegister = () => {
        navigate('/register');
    };

    const handleLogin = () => {
        navigate('/login');
    }

    return(
        <div className="container">
            <SubmitButton onClick={handleRegister}>Register</SubmitButton>
            <SubmitButton onClick={handleLogin}>Login</SubmitButton>
        </div>
    )
}

export default Home;