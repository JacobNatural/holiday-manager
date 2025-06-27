import SubmitButton from "./SubmitButton";

const changeEmailForm = ({onSubmit}) => {
    return <form className={"container"} id="change-email" onSubmit={onSubmit}>
        <label>Current password:</label>
        <input name="current-password" type="password" id="current-password"/>
        <label>New email:</label>
        <input name="new-email" type="email" id="new-email"/>
        <label>Confirm email:</label>
        <input name="confirm-email" type="email" id="confirm-email"/>
        <SubmitButton id={"change-email-button"}>change</SubmitButton>
    </form>
}

export default  changeEmailForm;