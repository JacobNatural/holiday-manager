import ChangeEmailForm from "../components/ChangeEmailForm";
import ErrorsContainer from "../components/ErrorsContainer";
import {useTimeDisplay} from "../hooks/useTimeDisplay";
import SendData from "../services/apiService";
import {ENDPOINTS} from "../api/ENDPOINTS";

const ChangeEmail = () => {

    const {error, display, showMessage} = useTimeDisplay();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData(e.target);
        const data = {
            currentPassword: formData.get('current-password'),
            newEmail: formData.get('new-email'),
            confirmEmail: formData.get('confirm-email')
        }

        SendData(data, ENDPOINTS.CHANGE_EMAIL, 'PATCH')
            .then(rep => {
                alert('successfully changed!');
                window.history.back();
            })
            .catch(err => {
                showMessage(err.message);
            });
    }

    return (<div>
        <ChangeEmailForm onSubmit={handleSubmit}/>
        <ErrorsContainer display={display}>{error}</ErrorsContainer>
    </div>)
}

export default ChangeEmail;