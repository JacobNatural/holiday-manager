import ChangePasswordForm from "../components/ChangePasswordForm";
import SendData from "../services/apiService";
import {useNavigate} from "react-router-dom";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import {ENDPOINTS} from "../api/ENDPOINTS";



const ChangePassword = () => {
    const navigate = useNavigate();
    const {error, display, showMessage} = useTimeDisplay();

    const handleChangePassword = async (e) => {
        e.preventDefault();
        const form = e.target;
        const dataForm = new FormData(form);

        const data = {
            currentPassword: dataForm.get('current-password'),
            newPassword: dataForm.get('new-password'),
            confirmPassword: dataForm.get('confirm-password')
        }
        await SendData(data, ENDPOINTS.CHANGE_PASSWORD, 'PATCH',null)
            .then(() => navigate('/login'))
            .catch(e => showMessage(e.message));
    }

    return (<div>
        <ChangePasswordForm onSubmit={handleChangePassword}/>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
        </div>
    );
}

export default ChangePassword;