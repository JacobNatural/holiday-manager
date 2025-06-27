import ActivateForm from "../components/ActivateForm";
import NewActivateTokenForm from "../components/NewActivateTokenForm";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import SendData from "../services/apiService";
import {ENDPOINTS} from "../api/ENDPOINTS";

const Activate = () => {
    const navigate = useNavigate();

    const [newTokenActivate, setNewTokenActivate] = useState('none');

    const handleNewToken = () => {
        setNewTokenActivate('grid')
    }

    const handleActivateToken = async (e) => {
        e.preventDefault();
        const form = new FormData(e.target);
        const data = {
            token: form.get('token')
        }
        await SendData(data, ENDPOINTS.USER_BASE,'PATCH', null)
            .then(res => {
                console.log(res);
                navigate('/login');
            } )
            .catch(err => console.log(err));
    }
    return (<div className={'container'}><ActivateForm  newToken={handleNewToken} onSubmit={handleActivateToken}/><NewActivateTokenForm onDisplay={newTokenActivate}/></div>)
}

export default Activate;