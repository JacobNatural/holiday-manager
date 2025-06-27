import SubmitButton from "./SubmitButton";

const ChangePasswordForm = ({onSubmit}) => {
    return <form className={"container"} id={"change-password"} onSubmit={onSubmit}>
        <label>Current password:</label>
        <input name="current-password" type="password" id="current-password"/>
        <label>New password:</label>
        <input name="new-password" type="password" id="new-password"/>
        <label>Confirm password:</label>
        <input name="confirm-password" type="password" id="confirm-password"/>
        <SubmitButton id={"change-password-button"}>Change</SubmitButton>
    </form>
}

export default ChangePasswordForm;
