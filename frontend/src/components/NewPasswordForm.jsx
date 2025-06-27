import SubmitButton from "./SubmitButton";

const NewPasswordForm = ({onSubmit}) => {
    return <form id="new-password-form" className="container" onSubmit={onSubmit}>

        <label htmlFor="token">Token:</label>
        <input type="text" id="token" name="token" required/>

        <label htmlFor="new-password">New password:</label>
        <input type="password" id="new-password" name="new-password" required/>

        <label htmlFor="confirm-password">Confirm password:</label>
        <input type="password" id="confirm-password" name="confirm-password" required/>

        <SubmitButton id={"get-token"}>New password</SubmitButton>
    </form>
}

export default NewPasswordForm;