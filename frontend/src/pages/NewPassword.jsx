import NewPasswordForm from "../components/NewPasswordForm";
import {useNavigate} from "react-router-dom";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import SendData from "../services/apiService";
import ErrorsContainer from "../components/ErrorsContainer";
import {ENDPOINTS} from "../api/ENDPOINTS";

const NewPassword = () => {

    const navigate = useNavigate();
    const {error, display, showMessage} = useTimeDisplay();

    const handleSubmit = (e) => {
        e.preventDefault();

        const dataForm = new FormData(e.target);

        const data = {
            token: dataForm.get('token'),
            newPassword: dataForm.get('new-password'),
            confirmPassword: dataForm.get('confirm-password')
        }

        SendData(data, ENDPOINTS.NEW_PASSWORD, 'PATCH')
            .then(() => navigate('/login'))
            .catch((e) => showMessage(e))
    }

    return (<div>
        <NewPasswordForm onSubmit={handleSubmit}/>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </div>)
}

export default NewPassword;